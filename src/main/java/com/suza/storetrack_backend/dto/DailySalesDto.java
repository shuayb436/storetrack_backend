package com.suza.storetrack_backend.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "quantity_sold", "sales_date", "product_id" })
public class DailySalesDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    private Integer quantitySold;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate salesDate;

    
    private Double selling_price;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double totalSale;

    // forward r/ship
    private Long product_id;
}
