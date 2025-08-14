package org.jithin.common;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A DTO that represents the OrderCreatedEvent consumed from Kafka.
 * The structure of this class MUST match the event produced by the order-service.
 * We add @JsonCreator and @JsonProperty to help Jackson with deserialization.
 */
public record OrderCreatedEvent(
        Long orderId,
        String customerId,
        ZonedDateTime orderDate,
        BigDecimal totalAmount,
        List<OrderItemData> items
) {
    public record OrderItemData(
            Long productId,
            int quantity,
            BigDecimal pricePerUnit
    ) {
        @JsonCreator
        public OrderItemData(
                @JsonProperty("productId") Long productId,
                @JsonProperty("quantity") int quantity,
                @JsonProperty("pricePerUnit") BigDecimal pricePerUnit
        ) {
            this.productId = productId;
            this.quantity = quantity;
            this.pricePerUnit = pricePerUnit;
        }
    }

    @JsonCreator
    public OrderCreatedEvent(
            @JsonProperty("orderId") Long orderId,
            @JsonProperty("customerId") String customerId,
            @JsonProperty("orderDate") ZonedDateTime orderDate,
            @JsonProperty("totalAmount") BigDecimal totalAmount,
            @JsonProperty("items") List<OrderItemData> items
    ) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items;
    }
}
