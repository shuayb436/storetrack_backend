package com.suza.storetrack_backend.model;

import java.util.ArrayList;
import java.util.List;

import com.suza.storetrack_backend.enums.ProductStatusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   

    private String name;

    private Integer quantity;

    private Double price;

    @Column(name = "min_quantity")
    private Integer minQuantity;

    @Enumerated(EnumType.STRING)
    private ProductStatusEnum status = ProductStatusEnum.AVAILABLE;


    // reverse r/ship
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DailySale> dailySales = new ArrayList<>();

    // reverse r/ship
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockHistory> stockHistories = new ArrayList<>();//product analist inaconsist dailysales

}

