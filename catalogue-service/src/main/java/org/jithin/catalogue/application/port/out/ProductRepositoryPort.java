package org.jithin.catalogue.application.port.out;

import org.jithin.catalogue.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(Long id);
}
