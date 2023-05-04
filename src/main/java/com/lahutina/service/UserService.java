package com.lahutina.service;

import com.lahutina.model.User;

import java.util.List;

public interface UserService {
    User readUser(Long chatId);

    User addUser(User user);

    List<User> readAllUsers();

    void deleteUser(User user);
}
