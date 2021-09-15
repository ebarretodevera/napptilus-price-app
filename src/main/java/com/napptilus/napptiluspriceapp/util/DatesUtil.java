package com.napptilus.napptiluspriceapp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class DatesUtil {

    @Value("${napptilus.dates.format}")
    private String dateTimeFormat;

    public LocalDateTime parseStringDate(String stringDate) {
        return LocalDateTime.parse(stringDate, DateTimeFormatter.ofPattern(dateTimeFormat));
    }

}
