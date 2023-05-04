package com.lahutina.bot;

import com.lahutina.model.User;
import com.lahutina.service.DateService;
import com.lahutina.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@EnableScheduling
public class SendDailyMessages extends TelegramLongPollingBot {
    private final DateService dateService;
    private final UserService userService;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.bot.username}")
    private String username;

    SendDailyMessages(DateService dateService, UserService userService) {
        this.dateService = dateService;
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            User user = userService.readUser(chatId).orElse(null);

            if (user == null) {
                userService.addUser(new User(chatId, username, true));
            }

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(String.format("Привіт, %s. Сповіщення надсилатимуться щодня о 12:00.", username))
                    .build();

            try {
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
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

    @Scheduled(cron = "0 0 12 * * *")
    public void sendDailyMessage() {
        List<User> userList = userService.readAllUsers();
        for (User user : userList) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(user.getUserId().toString())
                    .text(String.format("Від початку року пройшло: %.2f%% днів.",
                            dateService.getDaysFromCurrentDay()))
                    .build();

            try {
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}