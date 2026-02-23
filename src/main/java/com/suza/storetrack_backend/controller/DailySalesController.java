package com.suza.storetrack_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suza.storetrack_backend.dto.DailySalesDto;
import com.suza.storetrack_backend.service.DailySalesService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/v1/storetrack/dailysales")
public class DailySalesController {

    @Autowired
    private DailySalesService dailySalesService;
    
    // @GetMapping
    // public ResponseEntity<List<DailySalesDto>> getAllDailySales() {
    //     List<DailySalesDto> sales = dailySalesService.getAllDailySales();
    //     return new ResponseEntity<>(sales, HttpStatus.OK);
    // }
    @GetMapping
    public List<DailySalesDto> getAllDailySales() {
        return dailySalesService.getAllDailySales();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DailySalesDto> getDailySaleById(@PathVariable Long id) {
        DailySalesDto dto = dailySalesService.getdailySaleById(id);
        return ResponseEntity.ok(dto);
    }

    // @PostMapping
    // public ResponseEntity<DailySalesDto> addDailySale(@RequestBody DailySalesDto salesDto) {
    //     DailySalesDto dto = dailySalesService.addDailySale(salesDto);
    //     return new ResponseEntity<>(dto, HttpStatus.CREATED);
    // }
    @PostMapping
    public DailySalesDto addDailySale(@RequestBody DailySalesDto salesDto) {
        return dailySalesService.addDailySale(salesDto);
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<DailySalesDto> editDailySale(@PathVariable Long id, @RequestBody DailySalesDto salesDto) {
        DailySalesDto dto = dailySalesService.editDailySale(id,salesDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    } 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDailySale(@PathVariable Long id) {
        dailySalesService.deleteDailySale(id);
        return ResponseEntity.noContent().build();
    }
}
