package am.hitech.controller;

import am.hitech.model.User;
import am.hitech.model.dto.request.UserRequestDto;
import am.hitech.service.UserService;
import am.hitech.util.exceptions.DuplicateException;
import am.hitech.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<User> getById(@RequestParam int id) throws NotFoundException {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAuthority('HR') or hasAuthority('PM')")
    @GetMapping("/users/hr-pm")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = userService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getOnlyActiveUsers() {
        List<User> list = userService.getOnlyActiveUsers();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRequestDto requestDto) throws DuplicateException {
        userService.create(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String firstName,
                                    @RequestParam(required = false) String lastName,
                                    @RequestParam(required = false) String email) {
        List<User> list = userService.search(firstName, lastName, email);

        return ResponseEntity.ok(list);
    }

    @PatchMapping("/edit")
    public ResponseEntity<Void> edit(@RequestParam String name,
                                     @RequestParam String surname,
                                     Authentication authentication) {
        userService.edit(name, surname, authentication.getName());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit2")
    public ResponseEntity<Void> edit2(@RequestParam String name,
                                      @RequestParam String surname,
                                      Authentication authentication) {
        userService.edit2(name, surname, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('HR')")
    @PatchMapping("/change")
    public ResponseEntity<Void> change(@RequestParam int id) {
        userService.change(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/code")
    public ResponseEntity<Void> code(Authentication authentication) {
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        userService.code(code, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/verification")
    public ResponseEntity<Void> verification(@RequestParam String code, Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        if (code.equals(user.getVerificationCode())) {
            userService.verification(authentication.getName());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PatchMapping("/passwordToken")
    public ResponseEntity<Void> passwordToken(Authentication authentication) {
        userService.passwordToken(authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestParam int passwordToken,
                                               @RequestParam String password,
                                               @RequestParam String matchPassword,
                                               Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        if (passwordToken == user.getPasswordToken() && password.equals(matchPassword)) {
            userService.changePassword(passwordEncoder.encode(password),authentication.getName());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @GetMapping("/own-info")
    public ResponseEntity<User> getOwnInfo(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAuthority('HR')")
    @PatchMapping("/activate")
    public ResponseEntity<User> Activate(@RequestParam int id) throws NotFoundException {
        User user = userService.getById(id);
        userService.activate(user.getEmail());
        return ResponseEntity.ok().build();
    }
}
