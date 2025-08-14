package org.jithin.order.infrastructure.adapter.in.web;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        String customerId,
        List<OrderItemRequest> items
) {

    public record OrderItemRequest(
            Long productId,
            int quantity,
            BigDecimal pricePerUnit
    ) {}
}
