package com.napptilus.napptiluspriceapp.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class PricesCsvDto {

    @CsvBindByName(column="BrandId")
    private Long brandId;

    @CsvBindByName(column="StartDate")
    private String startDate;

    @CsvBindByName(column="EndDate")
    private String endDate;

    @CsvBindByName(column="PriceList")
    private Long priceList;

    @CsvBindByName(column="ProductId")
    private Long productId;

    @CsvBindByName(column="Priority")
    private Integer priority;

    @CsvBindByName(column="Price")
    private Double price;

    @CsvBindByName(column="Currency")
    private String currency;

    @CsvBindByName(column="LastUpdate")
    private String lastUpdate;

    @CsvBindByName(column="LastUpdateBy")
    private String lastUpdateBy;

}
