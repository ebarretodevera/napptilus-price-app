package com.napptilus.napptiluspriceapp.api;

import com.napptilus.napptiluspriceapp.exception.FilterException;
import com.napptilus.napptiluspriceapp.model.Prices;
import com.napptilus.napptiluspriceapp.service.PricesService;
import com.napptilus.napptiluspriceapp.util.DatesUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PricesRestControllerTest {

    @Autowired
    private DatesUtil datesUtil;
    @Autowired
    private PricesService pricesService;

    /**
     * Test 1: petición a las 10:00 del día 14 del producto 35455 (ZARA)
     */
    @Test
    void test1() throws FilterException {

        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = datesUtil.parseStringDate("2020-06-14-10.00.00");

        List<Prices> discounts = pricesService.findDiscountsOrderedByPriorityAtCertainDate(date, brandId, productId);
        assertEquals(1, discounts.size());
    }

    /**
     * Test 2: petición a las 16:00 del día 14 del producto 35455 (ZARA)
     */
    @Test
    void test2() throws FilterException {

        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = datesUtil.parseStringDate("2020-06-14-16.00.00");

        List<Prices> discounts = pricesService.findDiscountsOrderedByPriorityAtCertainDate(date, brandId, productId);
        assertEquals(2, discounts.size());

    }

    /**
     * Test 3: petición a las 21:00 del día 14 del producto 35455 (ZARA)
     */
    @Test
    void test3() throws FilterException {

        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = datesUtil.parseStringDate("2020-06-14-21.00.00");

        List<Prices> discounts = pricesService.findDiscountsOrderedByPriorityAtCertainDate(date, brandId, productId);
        assertEquals(1, discounts.size());

    }

    /**
     * Test 4: petición a las 10:00 del día 15 del producto 35455 (ZARA)
     */
    @Test
    void test4() throws FilterException {

        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = datesUtil.parseStringDate("2020-06-15-10.00.00");

        List<Prices> discounts = pricesService.findDiscountsOrderedByPriorityAtCertainDate(date, brandId, productId);
        assertEquals(2, discounts.size());

    }

    /**
     * Test 5: petición a las 21:00 del día 16 del producto 35455 (ZARA)
     */
    @Test
    void test5() throws FilterException {

        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = datesUtil.parseStringDate("2020-06-16-21.00.00");

        List<Prices> discounts = pricesService.findDiscountsOrderedByPriorityAtCertainDate(date, brandId, productId);
        assertEquals(2, discounts.size());

    }
}
