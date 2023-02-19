package com.ironhack.service.interfaces;
import com.ironhack.model.Users.User;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> getUsers();

}
