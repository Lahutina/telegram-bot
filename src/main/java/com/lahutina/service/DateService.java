package com.lahutina.service;

import com.lahutina.model.Date;

public interface DateService {

    double getDaysFromCurrentDay();
    double getDaysFromSpecificDay(Date date);
}
