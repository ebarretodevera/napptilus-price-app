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
public class Product {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "product", nullable = false)
    private String productName;

    public Product(Long id, String productName) {
        this.id = id;
        this.productName = productName;
    }
}
