package org.jithin.common;


import java.math.BigDecimal;
import java.util.List;

public record PaymentProcessedEvent(
        Long orderId,
        BigDecimal amount,
        PaymentStatus status,
        String transactionId,
        List<OrderCreatedEvent.OrderItemData> items
) {
}
