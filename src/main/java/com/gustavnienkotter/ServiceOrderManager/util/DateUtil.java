package com.gustavnienkotter.ServiceOrderManager.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class DateUtil {

    public Timestamp timestampNow() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public Timestamp formatDate(String date) {
        return Timestamp.valueOf("10-12-2020");
    }

}
