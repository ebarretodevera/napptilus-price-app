package com.napptilus.napptiluspriceapp.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class Filter {
    private String field;
    private QueryOperator operator;
    private ValueType valueType;
    private String value;
    private Boolean isString;
    private LocalDateTime dateValue;
    private Boolean isDate;
    private List<String> values;    //Used in case of IN operator
    private List<String> tables;
}
