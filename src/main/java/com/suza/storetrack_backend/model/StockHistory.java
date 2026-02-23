package com.suza.storetrack_backend.model;

import java.time.LocalDate;

import com.suza.storetrack_backend.enums.ChangeTypeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// @Data
@Getter
@Setter
@Entity
@Table(name = "stockhistory")
public class StockHistory {

   @Id 
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id; 

   // private String change_type; 
   @Enumerated(EnumType.STRING)
   private ChangeTypeEnum change_type;

   private Integer change_quantity; 

   private LocalDate change_date;

   //forward r/ship
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "product_id",nullable = false)
   private Product product;
}
