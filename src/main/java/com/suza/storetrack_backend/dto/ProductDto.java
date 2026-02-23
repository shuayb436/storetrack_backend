package com.suza.storetrack_backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.suza.storetrack_backend.enums.ProductStatusEnum;

import lombok.Data;

@Data
public class ProductDto {
  
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    private String name;
    
    private Integer quantity;
    
    private Double price;
    
    private Integer minQuantity;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProductStatusEnum status;

    // reverse r/ship
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> dailySales_ids;

    // reverse r/ship
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> stockHistory_ids;
}
