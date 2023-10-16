package am.hitech.service.impl;

import am.hitech.model.User;
import am.hitech.model.dto.request.UserRequestDto;
import am.hitech.repository.UserRepository;
import am.hitech.service.UserService;
import am.hitech.util.ErrorMessage;
import am.hitech.util.exceptions.DuplicateException;
import am.hitech.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getById(int id) throws NotFoundException {
        User user = userRepository.findById(id);
        if (user == null) {
             throw new NotFoundException("User not found for id " + id);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> list = userRepository.getAllBy();
        return list;
    }

    @Override
    public List<User> getOnlyActiveUsers() {
        return userRepository.getOnlyActiveUsers();
    }

    @Override
    public User getByUsername(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public void create(UserRequestDto requestDto) throws DuplicateException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateException(ErrorMessage.DUPLICATE_EMAIL);
        }
        User user = convertToUser(requestDto,new User());
        userRepository.save(user);
    }

    @Override
    public List<User> search(String firstName, String lastName, String email) {
       return userRepository.search(firstName,lastName,email);
    }

    @Override
    public void edit(String firstName, String lastName, String email) {
        Optional<String> optionalFirstName = Optional.ofNullable(firstName);
        Optional<String> optionalLastName = Optional.ofNullable(lastName);
        userRepository.edit(optionalFirstName,optionalLastName,email);
    }

    @Override
    public void edit2(String firstName, String lastName, String email) {
        userRepository.edit2(firstName,lastName,email);
    }

    @Override
    public void change(int id) {
        userRepository.change(id);
    }

    @Override
    public void code(String code, String email) {
        userRepository.code(code,email);
    }

    @Override
    public void verification(String email) {
        userRepository.verification(email);
    }

    @Override
    public void passwordToken(String email) {
        userRepository.passwordToken(ThreadLocalRandom.current().nextInt(100000,1000000),email);
    }

    @Override
    public void changePassword(String password, String email) {
        userRepository.updatePassword(password,email);
    }

    @Override
    public void activate(String email){
        userRepository.activate(email);
    }

    private User convertToUser(UserRequestDto requestDto,User user) {
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setStatus(requestDto.getStatus());
        user.setRole(requestDto.getRole());

        return user;
    }
}
