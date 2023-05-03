package com.lahutina.service.impl;

import com.lahutina.model.Date;
import com.lahutina.service.DateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class DateServiceImpl implements DateService {
    public double getDaysFromCurrentDay() {
        LocalDate currentDate = LocalDate.now();
        return calculateYearPassPercentage(currentDate);
    }

    public double getDaysFromSpecificDay(Date date) {
        if (date == null || date.getDate() == null) {
            return -1;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate inputDate = LocalDate.parse(date.getDate(), dtf);

        return calculateYearPassPercentage(inputDate);
    }

    private double calculateYearPassPercentage(LocalDate date) {

        double daysPassed = ChronoUnit.DAYS.between(LocalDate.of(date.getYear(), 1, 1), date) + 1;
        double totalDaysInYear = date.isLeapYear() ? 366 : 365;
        return daysPassed / totalDaysInYear * 100;
    }
}
