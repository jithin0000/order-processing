package org.jithin.order.infrastructure.adapter.in.web;


import org.jithin.order.domain.model.Order;
import org.jithin.order.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for the API response after creating an order.
 */
public record OrderResponse(
        Long orderId,
        String customerId,
        ZonedDateTime orderDate,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items
) {
    public record OrderItemResponse(
            Long productId,
            int quantity,
            BigDecimal pricePerUnit
    ) {}

    /**
     * A static factory method to convert a domain Order object to a response DTO.
     */
    public static OrderResponse fromDomain(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPricePerUnit()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                itemResponses
        );
    }
}