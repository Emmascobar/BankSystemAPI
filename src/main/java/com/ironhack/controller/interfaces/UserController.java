package com.ironhack.controller.interfaces;

import com.ironhack.model.Users.User;
import java.util.List;
public interface UserController {

    List<User> getUsers();
    void saveUser(User user);

}
