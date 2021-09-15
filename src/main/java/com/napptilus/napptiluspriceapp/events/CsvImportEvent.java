package com.napptilus.napptiluspriceapp.events;

import com.napptilus.napptiluspriceapp.dto.PricesCsvDto;
import com.napptilus.napptiluspriceapp.model.*;
import com.napptilus.napptiluspriceapp.service.*;
import com.napptilus.napptiluspriceapp.util.DatesUtil;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@Component
public class CsvImportEvent {

    @Autowired
    private BrandService brandService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PriceListService priceListService;
    @Autowired
    private PricesService pricesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DatesUtil datesUtil;

    @EventListener(ApplicationReadyEvent.class)
    public void importCsvData() {
        log.info("Importing csv prices data...");
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("csv/prices.csv").getFile());
            List<PricesCsvDto> data = new CsvToBeanBuilder(new FileReader(file)).withType(PricesCsvDto.class).build().parse();

            brandService.save(new Brand(1L, "ZARA"));

            for (int i = 1; i <= 4; i++)
                priceListService.save(new PriceList(MessageFormat.format("PRICE LIST {0}", i)));

            productService.save(new Product(35455L, "PRODUCT 35455"));

            currencyService.save(new Currency("EUR", "EUROS", "€"));

            if (data.isEmpty()) {
                log.info("No data...");
            } else {
                log.info(MessageFormat.format("Importando {0} items", data.size()));
                for (PricesCsvDto item : data) {
                    log.info("Importing new item...");
                    log.info(item.toString());

                    try {
                        Prices prices = new Prices();
                        prices.setBrand(brandService.findById(item.getBrandId()));
                        prices.setPriceList(priceListService.findById(item.getPriceList()));
                        prices.setProduct(productService.findById(item.getProductId()));
                        prices.setCurrency(currencyService.findById(item.getCurrency()));
                        prices.setStartDate(datesUtil.parseStringDate(item.getStartDate()));
                        prices.setEndDate(datesUtil.parseStringDate(item.getEndDate()));
                        prices.setLastUpdate(datesUtil.parseStringDate(item.getLastUpdate()));
                        prices.setPriority(item.getPriority());
                        prices.setPrice(item.getPrice());
                        prices.setLastUpdateBy(item.getLastUpdateBy());
                        pricesService.save(prices);
                    } catch (Exception e) {
                        log.error("Error importando item...");
                        log.error(e.getMessage());
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error importing data from csv file: " + e.getMessage());
        }
    }

}
