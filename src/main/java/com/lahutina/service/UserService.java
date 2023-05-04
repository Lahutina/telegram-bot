package com.lahutina.service;

import com.lahutina.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> readUser(Long chatId);

    User addUser(User user);

    List<User> readAllUsers();

    void deleteUser(User user);
}
