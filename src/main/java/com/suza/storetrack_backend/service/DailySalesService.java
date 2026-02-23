package com.suza.storetrack_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suza.storetrack_backend.dto.DailySalesDto;
import com.suza.storetrack_backend.enums.ChangeTypeEnum;
import com.suza.storetrack_backend.enums.ProductStatusEnum;
import com.suza.storetrack_backend.model.DailySale;
import com.suza.storetrack_backend.model.Product;
import com.suza.storetrack_backend.model.StockHistory;
import com.suza.storetrack_backend.repository.DailySalesRepository;
import com.suza.storetrack_backend.repository.ProductRepository;
import com.suza.storetrack_backend.repository.StockHistoryRepository;

import jakarta.transaction.Transactional;

@Service
public class DailySalesService {

    // @Autowired
    // private DailySalesRepository dailySalesRepository;
    private final DailySalesRepository dailySalesRepository;
    private final ProductRepository productRepository;
    private final StockHistoryRepository stockHistoryRepository;
    private final ModelMapper modelMapper;


    public DailySalesService(DailySalesRepository dailySalesRepository, ProductRepository productRepository,
            StockHistoryRepository stockHistoryRepository, ModelMapper modelMapper) {
        this.dailySalesRepository = dailySalesRepository;
        this.productRepository = productRepository;
        this.stockHistoryRepository = stockHistoryRepository;
        this.modelMapper = modelMapper;
    }

    // THIS RETURNS (FK)product_id=null - (u hv manually set it)
    // public List<DailySalesDto> getAllDailySales() {
    // List<DailySale> sales = dailySalesRepository.findAll();
    // return sales.stream()
    // .map(sale -> modelMapper.map(sale, DailySalesDto.class))
    // .collect(Collectors.toList());
    // }
    public List<DailySalesDto> getAllDailySales() {
        List<DailySale> sales = dailySalesRepository.findAll();
        return sales.stream()
                .map(sale -> {
                    DailySalesDto dto = modelMapper.map(sale, DailySalesDto.class);
                    // FK
                    if (sale.getProduct() != null) {
                        dto.setProduct_id(sale.getProduct().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

 
    public DailySalesDto getdailySaleById(Long id) {
        DailySale sale = dailySalesRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Daily sales not found with id " + id));
        DailySalesDto dto = modelMapper.map(sale, DailySalesDto.class);
        // FK
        if (sale.getProduct() != null) {
            dto.setProduct_id(sale.getProduct().getId());
        }
        return dto;
    }

    // public DailySalesDto addDailySale(DailySalesDto salesDto) {

    // // 1. Map simple fields ONLY
    // DailySale sales = modelMapper.map(salesDto, DailySale.class);

    // // 2. Handle FK manually (VERY IMPORTANT)
    // if (salesDto.getProduct_id() == null) {
    // throw new IllegalStateException("Product ID is required");
    // }

    // Product product = productRepository.findById(salesDto.getProduct_id())
    // .orElseThrow(() -> new IllegalStateException("Product not found with id " +
    // salesDto.getProduct_id()));

    // sales.setProduct(product);

    // // 3. Save entity
    // DailySale savedSale = dailySalesRepository.save(sales);

    // // 4. Map back to DTO
    // DailySalesDto response = modelMapper.map(savedSale, DailySalesDto.class);

    // // 5. Manually set FK in DTO
    // response.setProduct_id(savedSale.getProduct().getId());

    // return response;
    // }
    
    @Transactional //if any step fails, everything  rolls back
public DailySalesDto addDailySale(DailySalesDto salesDto) {

    DailySale sales = modelMapper.map(salesDto, DailySale.class);

    if (salesDto.getProduct_id() == null) {
        throw new IllegalStateException("Product ID is required");
    }

    Product product = productRepository.findById(salesDto.getProduct_id())
            .orElseThrow(() -> new IllegalStateException(
                    "Product not found with id " + salesDto.getProduct_id()));

    // ✅ Stock validation
    if (product.getQuantity() <= 0) {
        throw new IllegalStateException("Product out of stock");
    }

    if (salesDto.getQuantitySold() > product.getQuantity()) {
        throw new IllegalStateException("Insufficient stock");
    }

    // ✅ Reduce stock
    product.setQuantity(product.getQuantity() - salesDto.getQuantitySold());

    // ✅ Auto product status
    if (product.getQuantity() == 0) {
        product.setStatus(ProductStatusEnum.OUT_OF_STOCK);
    } else if (product.getQuantity() <= product.getMinQuantity()) {
        product.setStatus(ProductStatusEnum.LOW_STOCK);
    } else {
        product.setStatus(ProductStatusEnum.AVAILABLE);
    }

    productRepository.save(product);

    // ✅ FK
    sales.setProduct(product);

    // ✅ ALWAYS get price from product
    sales.setSelling_price(product.getPrice());

    // ✅ BACKEND total calculation
    sales.setTotalSale(
        sales.getQuantitySold() * sales.getSelling_price()
    );

    // ✅ Date fallback
    if (sales.getSalesDate() == null) {
        sales.setSalesDate(LocalDate.now());
    }

    DailySale savedSale = dailySalesRepository.save(sales);

    // ✅ Stock history
    StockHistory history = new StockHistory();
    history.setChange_type(ChangeTypeEnum.DECREASE);
    history.setChange_quantity(sales.getQuantitySold());
    history.setChange_date(LocalDate.now());
    history.setProduct(product);

    stockHistoryRepository.save(history);

    // ✅ Response DTO
    DailySalesDto response = modelMapper.map(savedSale, DailySalesDto.class);
    response.setProduct_id(product.getId());

    return response;
}

    // public DailySalesDto addDailySale(DailySalesDto salesDto) {

    //     // 1. Convert DTO to entity
    //     DailySale sales = modelMapper.map(salesDto, DailySale.class);

    //     // 2. Handle FK manually (VERY IMPORTANT)/Validate product id
    //     if (salesDto.getProduct_id() == null) {
    //         throw new IllegalStateException("Product ID is required");
    //     }

    //     // 3. Get product from database
    //     Product product = productRepository.findById(salesDto.getProduct_id())
    //             .orElseThrow(() -> new IllegalStateException("Product not found with id " + salesDto.getProduct_id()));

    //     // 4. Check stock
    //     if (product.getQuantity() < salesDto.getQuantitySold()) {
    //         throw new IllegalStateException("Insufficient stock");
    //     }

    //     // 5. Reduce product quantity(wen sales is made-whcich reduces product quantity)
    //     product.setQuantity(product.getQuantity() - salesDto.getQuantitySold());

    //     //6. Update product status automatically
    //     if (product.getQuantity() == 0) {
    //         product.setStatus(ProductStatusEnum.OUT_OF_STOCK);
    //     }else if(product.getQuantity() <= product.getMinQuantity()){
    //         product.setStatus(ProductStatusEnum.LOW_STOCK);
    //     }else{
    //         product.setStatus(ProductStatusEnum.AVAILABLE);

    //     }
    //     // 7. Save product updates
    //     productRepository.save(product);

    //     // 6. Set product to sale (FK)
    //     sales.setProduct(product);

    //     // Auto set selling price frm product price
    //     // sales.setSelling_price(product.getPrice());
        
    //     // 7. set total sale
    //     sales.setTotalSale(salesDto.getQuantitySold() * salesDto.getSelling_price());

    //     // 8. set sales date if not provided
    //     if (sales.getSalesDate() == null) {
    //         sales.setSalesDate(LocalDate.now());
    //     }
    //     // 9. Save daily sale
    //     DailySale savedSale = dailySalesRepository.save(sales);

    //     // 10. Create stock history record automatically
    //     StockHistory stockHistory = new StockHistory();
    //     stockHistory.setChange_type(ChangeTypeEnum.DECREASE);
    //     stockHistory.setChange_quantity(salesDto.getQuantitySold());
    //     stockHistory.setChange_date(LocalDate.now());
    //     stockHistory.setProduct(product);

    //     stockHistoryRepository.save(stockHistory);

    //     // 11. Map response back to DTO
    //     DailySalesDto response = modelMapper.map(savedSale, DailySalesDto.class);
    //     response.setProduct_id(product.getId());

    //     return response;

    // }

    public DailySalesDto editDailySale(Long id, DailySalesDto salesDto) {
        // fetch existing dailySale
        DailySale existingSale = dailySalesRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Daily sales with id " + id + " not found"));
        // Fetch product(fk)
        if (salesDto.getProduct_id() != null) {
            Product product = productRepository.findById(salesDto.getProduct_id())
                    .orElseThrow(() -> new IllegalStateException("Product with id " + id + " not found"));

            existingSale.setProduct(product);

        }

        // Map all fields from DTO to entity except product
        modelMapper.map(salesDto, existingSale);
        // save updated dailySale
        DailySale savedSale = dailySalesRepository.save(existingSale);

        // Map saved entity back to DTO for response
        DailySalesDto response = modelMapper.map(savedSale, DailySalesDto.class);

        if (savedSale.getProduct() != null) {
            response.setProduct_id(savedSale.getProduct().getId());
        }
        return response;

    }

    public void deleteDailySale(Long id) {
        dailySalesRepository.deleteById(id);
    }
//DELETING PRODUCT SHOULD ALSO DELETE STOCK HISTORY AND SALE
    // public void deleteDailySale(Long id) {
    //     StockHistory history = stockHistoryRepository.fi
    // }

}
