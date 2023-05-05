package com.lahutina.bot.commands;

import com.lahutina.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.lahutina.bot.commands.CommandName.START;
import static com.lahutina.bot.commands.CommandName.STOP;

@Component
public class CommandContainer {
    private final Map<String, Command> commandMap;

    public CommandContainer(UserService userService) {
        commandMap = new HashMap<>();
        commandMap.put(START.getCommandName(), new StartCommand(userService));
        commandMap.put(STOP.getCommandName(), new StopCommand(userService));
    }

    public Command retrieveCommand(String commandName) {
        return commandMap.get(commandName);
    }
}