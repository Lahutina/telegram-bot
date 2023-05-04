package com.lahutina.service.impl;

import com.lahutina.model.User;
import com.lahutina.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final List<User> users = new ArrayList<>();

    @Override
    public User readUser(Long chatId) {
        return users.stream()
                .filter(u-> u.getUserId().equals(chatId))
                .findFirst().orElse(null);
    }

    @Override
    public User addUser(User user) {
        if (user == null)
            return null;

        boolean isPresent = users.stream()
                .anyMatch(u -> u.getUserId().equals(user.getUserId()));
        if (!isPresent) {
            users.add(user);
            return user;
        } else return null;
    }


    @Override
    public void deleteUser(User user) {
        if (user != null)
            users.remove(user);
    }

    @Override
    public List<User> readAllUsers() {
        if(users.isEmpty())
            return null;
        return users;
    }
}
