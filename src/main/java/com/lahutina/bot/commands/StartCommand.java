package com.lahutina.bot.commands;

import com.lahutina.service.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StartCommand implements Command {
    private final UserService userService;

    @Override
    public void execute(Long userId) {
        userService.setStatus(userId, true);
    }
}
