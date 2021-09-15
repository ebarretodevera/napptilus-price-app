package com.napptilus.napptiluspriceapp.api;

import com.napptilus.napptiluspriceapp.dto.ApiResponseDto;
import com.napptilus.napptiluspriceapp.dto.DiscountToApplyDto;
import com.napptilus.napptiluspriceapp.service.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/prices")
public class PricesRestController {

    @Value("${napptilus.api.list-data.msg}")
    private String apiResponseListDataMsg;
    @Value("${napptilus.api.empty-data.msg}")
    private String apiResponseEmptyDataMsg;

    @Autowired
    private DiscountService discountService;

    @GetMapping("discounts")
    public ApiResponseDto getDiscountToApply(@RequestParam("date") String date,
                                             @RequestParam("brandId") Long brandId,
                                             @RequestParam("productId") Long productId) {
        ApiResponseDto apiResponse = new ApiResponseDto();
        try {
            List<DiscountToApplyDto> res = discountService.getDiscountToApply(date, brandId, productId);
            if (res.isEmpty())
                apiResponse.setResponseMsg(apiResponseEmptyDataMsg);
            else
                apiResponse.setResponseMsg(MessageFormat.format(apiResponseListDataMsg, res.size()));
            apiResponse.setData(res);
        } catch (Exception e) {
            apiResponse.setResponseException(e.getMessage());
        }
        return apiResponse;
    }
}
