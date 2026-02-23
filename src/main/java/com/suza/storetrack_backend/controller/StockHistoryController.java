package com.suza.storetrack_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suza.storetrack_backend.dto.StockHistoryDto;
import com.suza.storetrack_backend.service.StockHistoryService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/storetrack/stockhistory")
public class StockHistoryController {

    @Autowired
    private StockHistoryService stockHistoryService;

    @GetMapping
    public ResponseEntity<List<StockHistoryDto>> getAllStockHistory() {
        List<StockHistoryDto> stockHist = stockHistoryService.getAllStockHistory();
        return new ResponseEntity<>(stockHist, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockHistoryDto> getStockHistoryById(@PathVariable Long id) {
        StockHistoryDto stockHistory = stockHistoryService.getStockHistoryById(id);
        return ResponseEntity.ok(stockHistory);
    }

    @PostMapping
    public ResponseEntity<StockHistoryDto> addStockHistory(@RequestBody StockHistoryDto stockDto) {
        StockHistoryDto dto = stockHistoryService.addStockHistory(stockDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<StockHistoryDto> editStockHistory(@PathVariable Long id,
    //         @RequestBody StockHistoryDto stockDto) {
    //     StockHistoryDto dto = stockHistoryService.editStockHistory(id, stockDto);
    //     return new ResponseEntity<>(dto, HttpStatus.OK);
    // }

    @DeleteMapping("/{id}")
    public void deleteStockHistory(@PathVariable Long id) {
        stockHistoryService.deleteStockHistory(id);
    }

}
