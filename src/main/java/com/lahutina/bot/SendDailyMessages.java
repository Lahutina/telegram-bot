package com.lahutina.bot;

import com.lahutina.bot.commands.CommandContainer;
import com.lahutina.model.User;
import com.lahutina.service.DateService;
import com.lahutina.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SendDailyMessages extends TelegramLongPollingBot {
    private final DateService dateService;
    private final UserService userService;
    private final CommandContainer commandContainer;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.bot.username}")
    private String username;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            Optional<User> user = userService.readUser(chatId);

            if (user.isEmpty()) {
                userService.addUser(new User(chatId, username, true));
            }

            String userMessage = update.getMessage().getText();

            if (userMessage.startsWith("/")) {
                String commandName = userMessage.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandName).execute(chatId);
            } else {
                sendMessage(chatId, String.format("Привіт, %s. Сповіщення надсилатимуться щодня о 12:00.", username));
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Scheduled(cron = "0 55 21 * * *")
    public void sendDailyMessage() {
        List<User> userList = userService.readAllEnabledUsers();

        userList.forEach(user -> sendMessage(user.getUserId(),
                        String.format("Від початку року пройшло: %.2f%% днів.",
                        dateService.getDaysFromCurrentDay())));
    }

    public void sendMessage(Long userId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(userId.toString())
                .text(message)
                .build();
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}