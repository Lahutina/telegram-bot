package com.lahutina.bot;

import com.lahutina.service.DateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class SendDailyMessages extends TelegramLongPollingBot {
    private final DateService dateService;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.bot.username}")
    private String username;

    private Long userId;

    SendDailyMessages(DateService dateService) {
        this.dateService = dateService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            userId = update.getMessage().getChatId();
            setTimer();

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(userId.toString())
                    .text(String.format("Привіт, %s. Сповіщення надсилатимуться щодня о 12:00",
                                    update.getMessage().getFrom().getUserName()))
                    .build();

            try {
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setTimer() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendDailyMessage();
            }
        }, calendar.getTime(), 24 * 60 * 60 * 1000); // Виконується кожний день

    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

//    @Scheduled(cron = "0 0 12 * * *")
    public void sendDailyMessage() {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(userId.toString())
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