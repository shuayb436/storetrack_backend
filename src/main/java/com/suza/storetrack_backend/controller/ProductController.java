package com.suza.storetrack_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suza.storetrack_backend.dto.ProductDto;
import com.suza.storetrack_backend.dto.RestockRequestDto;
import com.suza.storetrack_backend.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController //inamwambia springboot hii class n y Cntrl na kaz yke inahusiana n kucreate api inahusian n product
@RequestMapping("/api/v1/storetrack/product")  //mwanzo wa api inaanza ivi
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping
    public List<ProductDto> getAllProduct() { //list ni return type ifanane na dto yko
        List<ProductDto> products = productService.getAllProduct();
        return products;
    }

  
    @PostMapping
    public ProductDto addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }
  
    // @PostMapping("/{id}/restock")
    // public ProductDto restockProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
    //     return productService.restockProduct(id,productDto);
    // }

    @PutMapping("/{id}")
    public ProductDto editProductById(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.editProduct(id, productDto);
         
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }


}
