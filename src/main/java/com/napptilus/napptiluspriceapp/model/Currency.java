package com.napptilus.napptiluspriceapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Currency {

    @Id
    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "currency_name", nullable = false, unique = true)
    private String currencyName;

    @Column(name = "symbol", nullable = false, unique = true)
    private String symbol;

    public Currency(String currencyCode, String currencyName, String symbol) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.symbol = symbol;
    }

}
