package com.suza.storetrack_backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
// @Getter
// @Setter
@Entity
@Table(name = "dailysale")
public class DailySale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    @Column(name = "sales_date")
    private LocalDate salesDate;
    
    private Double selling_price;
    private Double totalSale;


    // forward r/ship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;//list anaproduct1
}
