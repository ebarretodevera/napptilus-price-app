package com.napptilus.napptiluspriceapp.dto;

import lombok.Data;

@Data
public class ApiResponseDto {
    private String responseMsg;
    private String responseException;
    private Object data;
}
