package org.jithin.catalogue.infrastructure.adapter.in.web;

import org.jithin.catalogue.domain.model.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id, String name, String description, BigDecimal price, int stockQuantity
) {

    public static ProductResponse fromDomain(Product product)
    {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }
}
