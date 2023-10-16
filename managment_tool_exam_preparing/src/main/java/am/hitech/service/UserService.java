package am.hitech.service;

import am.hitech.model.User;
import am.hitech.model.dto.request.UserRequestDto;
import am.hitech.util.exceptions.DuplicateException;
import am.hitech.util.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService {
    User getById(int id) throws NotFoundException;

    List<User> getAll();

    List<User> getOnlyActiveUsers();

    User getByUsername(String email);

    void create(UserRequestDto requestDto) throws DuplicateException;

    List<User> search(String firstName, String lastName, String email);

    void edit(String firstName, String lastName, String email);

    void edit2(String firstName, String lastName, String email);

    void change(int id);

    void code(String code, String email);

    void verification(String email);

    void passwordToken(String email);

    void changePassword(String password, String email);

    void activate(String email);
}
