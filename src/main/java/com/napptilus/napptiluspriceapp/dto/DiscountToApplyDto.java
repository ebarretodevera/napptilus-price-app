package com.napptilus.napptiluspriceapp.dto;

import lombok.Data;

@Data
public class DiscountToApplyDto {

    private String productId;
    private String brandId;
    private String priceListId;
    private String startDate;
    private String endDate;
    private String price;
}
