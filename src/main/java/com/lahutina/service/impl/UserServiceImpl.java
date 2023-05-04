package com.lahutina.service.impl;

import com.lahutina.model.User;
import com.lahutina.repository.UserRepository;
import com.lahutina.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> readUser(Long chatId) {
        return userRepository.findById(chatId);
    }

    @Override
    public User addUser(User user) {
        if (user == null)
            return null;

        boolean isPresent = userRepository.existsById(user.getUserId());

        if (!isPresent) {
            return userRepository.save(user);
        } else return null;
    }


    @Override
    public void deleteUser(User user) {
        if (user != null)
            userRepository.delete(user);
    }

    @Override
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }
}
