package com.suza.storetrack_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suza.storetrack_backend.dto.StockHistoryDto;
import com.suza.storetrack_backend.enums.ChangeTypeEnum;
import com.suza.storetrack_backend.enums.ProductStatusEnum;
import com.suza.storetrack_backend.model.Product;
import com.suza.storetrack_backend.model.StockHistory;
import com.suza.storetrack_backend.repository.ProductRepository;
import com.suza.storetrack_backend.repository.StockHistoryRepository;

@Service
public class StockHistoryService {

    // @Autowired
    // private StockHistoryRepository stockHistoryRepository;
    // @Autowired
    // private ProductRepository productRepository;
    // @Autowired
    // private ModelMapper modelMapper;

    // OR
    private final StockHistoryRepository stockHistoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public StockHistoryService(StockHistoryRepository stockHistoryRepository, ProductRepository productRepository,
            ModelMapper modelMapper) {
        this.stockHistoryRepository = stockHistoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public List<StockHistoryDto> getAllStockHistory() {
        List<StockHistory> stocks = stockHistoryRepository.findAll();
        return stocks.stream()
                .map(stock -> {
                    StockHistoryDto dto = modelMapper.map(stock, StockHistoryDto.class);

                    if (stock.getProduct() != null) {
                        dto.setProduct_id(stock.getProduct().getId());
                    }

                    return dto;

                })
                .collect(Collectors.toList());
    }

    public StockHistoryDto getStockHistoryById(Long id) {
        StockHistory stockHis = stockHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("StockHistory not with id " + id));
        StockHistoryDto dto = modelMapper.map(stockHis, StockHistoryDto.class);
        if (stockHis.getProduct() != null) {
            dto.setProduct_id(stockHis.getProduct().getId());
        }
        return dto;
    }

    // public StockHistoryDto addStockHistory(StockHistoryDto stockDto) {

    // StockHistory stockHistory = modelMapper.map(stockDto, StockHistory.class);

    // if (stockDto.getProduct_id() == null) {
    // throw new IllegalStateException("Product ID is required");
    // }
    // Product product = productRepository.findById(stockDto.getProduct_id())
    // .orElseThrow(() -> new IllegalStateException("Product not found with id " +
    // stockDto.getProduct_id()));

    // stockHistory.setProduct(product);

    // // stockHistory.setChange_date(LocalDateTime.now());

    // StockHistory savedStockHistory = stockHistoryRepository.save(stockHistory);
    // // return modelMapper.map(savedStockHistory, StockHistoryDto.class);
    // StockHistoryDto response = modelMapper.map(savedStockHistory,
    // StockHistoryDto.class);
    // response.setProduct_id(savedStockHistory.getProduct().getId());
    // return response;

    // }

    public StockHistoryDto addStockHistory(StockHistoryDto stockDto) {

        // StockHistory stockHistory = modelMapper.map(stockDto, StockHistory.class);

        // 1. Validate product ID
        if (stockDto.getProduct_id() == null) {
            throw new IllegalStateException("Product ID is required");
        }
        // 2. Fetch product
        Product product = productRepository.findById(stockDto.getProduct_id())
                .orElseThrow(() -> new IllegalStateException("Product not found with id " + stockDto.getProduct_id()));

        // 3. Apply stock change using ENUM
        if (stockDto.getChange_type() == ChangeTypeEnum.INCREASE) {
            product.setQuantity(product.getQuantity() + stockDto.getChange_quantity());
        } else if (stockDto.getChange_type() == ChangeTypeEnum.DECREASE) {
            if (product.getQuantity() < stockDto.getChange_quantity()) {
                throw new IllegalStateException("Insufficient stock");
            }
            product.setQuantity(product.getQuantity() - stockDto.getChange_quantity());
        }

        // 4. Update product status
        if (product.getQuantity() == 0) {
            product.setStatus(ProductStatusEnum.OUT_OF_STOCK);
        } else if (product.getQuantity() <= product.getMinQuantity()) {
            product.setStatus(ProductStatusEnum.LOW_STOCK);
        } else {
            product.setStatus(ProductStatusEnum.AVAILABLE);
        }

        // SAVE PRODUCT (IMPORTANT)
        productRepository.save(product);

        // 6. Create stock history record
        StockHistory stockHistory = modelMapper.map(stockDto, StockHistory.class);
        stockHistory.setProduct(product);

        // auto-set date if missing
        if (stockHistory.getChange_date() == null) {
            stockHistory.setChange_date(LocalDate.now());
        }

        StockHistory savedStockHistory = stockHistoryRepository.save(stockHistory);

        // 7. Prepare response DTO
        StockHistoryDto response = modelMapper.map(savedStockHistory, StockHistoryDto.class);
        response.setProduct_id(product.getId());

        return response;

    }
// DISABLE UPDATE Stock history should be immutable (read-only). bcz Real systems never edit history
    // public StockHistoryDto editStockHistory(Long id, StockHistoryDto stockDto) {
    //     // fetch existing stockHistory
    //     StockHistory stockHistory = stockHistoryRepository.findById(id)
    //             .orElseThrow(() -> new IllegalStateException("StockHistory with id " + id + " not found"));
    //     // Update product only if provided
    //     if (stockDto.getProduct_id() != null) {
    //         Product product = productRepository.findById(stockDto.getProduct_id())
    //                 .orElseThrow(() -> new IllegalStateException(
    //                         "Product with id " + stockDto.getProduct_id() + " not found"));
    //         stockHistory.setProduct(product);
    //     }

    //     // Map all fields from DTO to entity except product
    //     modelMapper.map(stockDto, stockHistory);
    //     // save updated stockHistory
    //     StockHistory savedStockHistory = stockHistoryRepository.save(stockHistory);

    //     // Map saved entity back to DTO
    //     StockHistoryDto response = modelMapper.map(savedStockHistory, StockHistoryDto.class);
    //     if (savedStockHistory.getProduct() != null) {
    //         response.setProduct_id(savedStockHistory.getProduct().getId());
    //     }

    //     return response;

    // }

    public void deleteStockHistory(Long id) {
        stockHistoryRepository.deleteById(id);
    }

}
// {
// "change_type": "INCREASE",
// "change_quantity": 15,
// "change_date": "2026-01-12",
// "product_id": 1
// }
