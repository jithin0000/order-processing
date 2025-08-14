package org.jithin.catalogue.application.port.in;

import org.jithin.catalogue.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductUseCase {

    Optional<Product> getProductById(Long id);
    List<Product> findAllProducts();
    Product updateProductStock(Long productId, int quantity);
    void processStockUpdate(List<ItemStockUpdate> itemsToUpdate);
    record ItemStockUpdate(Long productId, int quantity){}
}
