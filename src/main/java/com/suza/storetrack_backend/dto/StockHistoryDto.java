package com.suza.storetrack_backend.dto;

import java.time.LocalDate;
// import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.suza.storetrack_backend.enums.ChangeTypeEnum;

import lombok.Data;

@Data
public class StockHistoryDto {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id; 

    private ChangeTypeEnum change_type; 

    private Integer change_quantity; 

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate change_date;
   
    //forward r/ship
     private Long product_id;
    
}
