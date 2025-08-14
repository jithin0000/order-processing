package org.jithin.catalogue.application.port.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jithin.catalogue.application.port.in.ProductUseCase;
import org.jithin.catalogue.domain.model.Product;
import org.jithin.catalogue.application.port.out.ProductRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ProductService implements ProductUseCase {
    private final ProductRepositoryPort productRepositoryPort;

    public ProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<Product> getProductById(Long id) {
        return productRepositoryPort.findById(id) ;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepositoryPort.findAll();
    }

    @Override
    public Product updateProductStock(Long productId, int quantity) {
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("Product not found with id "+productId));

        if(quantity<0)
            throw new IllegalArgumentException("Stock Quantity Cannot be negative");
        product.setStockQuantity(quantity);

        return productRepositoryPort.save(product);
    }

    @Override
    public void processStockUpdate(List<ItemStockUpdate> itemsToUpdate) {
        log.info("Processing stock update for {} items", itemsToUpdate.size());

        for (ItemStockUpdate item : itemsToUpdate)
        {
            try {
                Product product = productRepositoryPort.findById(item.productId())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found for stock update: " + item.productId()));

                int newStock = product.getStockQuantity() - item.quantity();
                if (newStock < 0) {
                    // This is a critical issue. An order was approved for which we don't have stock.
                    // This indicates a potential race condition or inconsistency.
                    log.error("CRITICAL: Insufficient stock for product {}. Current: {}, Required: {}. Stock not updated.",
                            item.productId(), product.getStockQuantity(), item.quantity());
                    // In a real system, this would trigger an alert and compensating action.
                    continue; // Skip this item and process the next.
                }

                product.setStockQuantity(newStock);
                productRepositoryPort.save(product);
                log.info("Updated stock for product {}. New quantity: {}", product.getId(), newStock);

            } catch (Exception e) {
                log.error("Failed to process stock update for productId: {}", item.productId(), e);
                // Continue processing other items even if one fails.
            }
        }

    }
}





















