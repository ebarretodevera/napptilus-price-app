package com.napptilus.napptiluspriceapp;

import com.napptilus.napptiluspriceapp.dto.PricesCsvDto;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.FileReader;
import java.util.List;

@Slf4j
@SpringBootApplication
public class NapptilusPriceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NapptilusPriceAppApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void appStarted() {
        log.info("NapptilusPriceAppApplication - ON");
    }

}
