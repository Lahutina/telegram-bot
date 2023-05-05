package com.lahutina.bot.commands;

import com.lahutina.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
public class StopCommand implements Command{
    private final UserService userService;

    @Override
    public void execute(Long userId) {
        userService.setStatus(userId, false);
    }
}
