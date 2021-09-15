package com.napptilus.napptiluspriceapp.service;

import com.napptilus.napptiluspriceapp.dto.DiscountToApplyDto;
import com.napptilus.napptiluspriceapp.exception.FilterException;
import com.napptilus.napptiluspriceapp.model.Prices;
import com.napptilus.napptiluspriceapp.util.DatesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DiscountService {

    @Value("${napptilus.service.exception.msg}")
    private String serviceExceptionMsg;

    @Autowired
    private PricesService pricesService;
    @Autowired
    private DatesUtil datesUtil;

    public List<DiscountToApplyDto> getDiscountToApply(String date, Long brandId, Long productId) throws FilterException {
        List<DiscountToApplyDto> pricesResult = new ArrayList<>();
        try {
            for (Prices price : pricesService.findDiscountsOrderedByPriorityAtCertainDate(datesUtil.parseStringDate(date), brandId, productId)) {
                DiscountToApplyDto discountToApplyDto = new DiscountToApplyDto();
                discountToApplyDto.setProductId(String.valueOf(price.getProduct().getId()));
                discountToApplyDto.setBrandId(String.valueOf(price.getBrand().getId()));
                discountToApplyDto.setPriceListId(String.valueOf(price.getPriceList().getId()));
                discountToApplyDto.setStartDate(String.valueOf(price.getStartDate()));
                discountToApplyDto.setEndDate(String.valueOf(price.getEndDate()));
                discountToApplyDto.setPrice(String.valueOf(price.getPrice()));
                pricesResult.add(discountToApplyDto);
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
        return pricesResult;
    }
}
