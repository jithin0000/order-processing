package org.jithin.payment.application.port.in;


import org.jithin.common.OrderCreatedEvent;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentUseCase {

    void processPayment(Long orderId, BigDecimal amount, List<OrderCreatedEvent.OrderItemData> items);
}
