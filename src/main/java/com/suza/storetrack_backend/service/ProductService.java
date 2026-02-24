package com.suza.storetrack_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.suza.storetrack_backend.dto.ProductDto;
import com.suza.storetrack_backend.enums.ChangeTypeEnum;
import com.suza.storetrack_backend.enums.ProductStatusEnum;
import com.suza.storetrack_backend.model.DailySale;
import com.suza.storetrack_backend.model.Product;
import com.suza.storetrack_backend.model.StockHistory;
import com.suza.storetrack_backend.repository.ProductRepository;
import com.suza.storetrack_backend.repository.StockHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
        
        private ProductRepository productRepository;
        private ModelMapper modelMapper; 
        
        private StockHistoryRepository stockHistoryRepository;
       

        public List<ProductDto> getAllProduct() {
                List<Product> products = productRepository.findAll();
                return products.stream()
                                .map(prod -> {
                                        ProductDto productDto = modelMapper.map(prod, ProductDto.class);
                                        // reverse r/ship mapping(set jxt the id not the whole dailSales and
                                        // stockHistory object)
                                        productDto.setDailySales_ids(prod.getDailySales() != null
                                                        ? prod.getDailySales().stream()
                                                                        .map(sale -> sale.getId()) 
                                                                        // OR
                                                                        // .map(DailySale::getId)
                                                                        .collect(Collectors.toList())
                                                        // : null);
                                                        : List.of()); // empty list instead ofnull
                                        productDto.setStockHistory_ids(prod.getStockHistories() != null
                                                        ? prod.getStockHistories().stream()
                                                                        .map(StockHistory::getId)
                                                                        .collect(Collectors.toList())
                                                        : List.of());
                                        return productDto;
                                }).collect(Collectors.toList());

        }


        public ProductDto addProduct(ProductDto productDto) {
                Product prod = modelMapper.map(productDto, Product.class);

                Product savedProduct = productRepository.save(prod);

                // CREATE INITIAL STOCK HISTORY
                if (savedProduct.getQuantity() > 0) {
                        StockHistory history = new StockHistory();
                        history.setChange_type(ChangeTypeEnum.INCREASE);
                        history.setChange_quantity(savedProduct.getQuantity());
                        history.setChange_date(LocalDate.now());
                        history.setProduct(savedProduct);

                        stockHistoryRepository.save(history);


                }
                ProductDto proDto = modelMapper.map(savedProduct, ProductDto.class);

                proDto.setDailySales_ids(prod.getDailySales() != null
                                ? prod.getDailySales().stream()
                                                .map(DailySale::getId)
                                                .collect(Collectors.toList())
                                : List.of());
                proDto.setStockHistory_ids(prod.getStockHistories() != null
                                ? prod.getStockHistories().stream()
                                                .map(StockHistory::getId)
                                                .collect(Collectors.toList())
                                : List.of());
                return proDto;

        }
 
        public ProductDto editProduct(Long id, ProductDto productDto) {
                // Fetch existing application
                Product existingProd = productRepository.findById(id)
                                .orElseThrow(() -> new IllegalStateException("Product not found with id " + id));

                // productDto.setId(existingProd.getId());
                // Map simple fields first
                modelMapper.map(productDto, existingProd);

                // Map relationships(FK if any) after ModelMapper to prevent overwriting

                // Save and return DTO
                Product savedProduct = productRepository.save(existingProd);

                return modelMapper.map(savedProduct, ProductDto.class);

        }

        public void deleteProductById(Long id) {
                productRepository.deleteById(id);
        }

       

}
