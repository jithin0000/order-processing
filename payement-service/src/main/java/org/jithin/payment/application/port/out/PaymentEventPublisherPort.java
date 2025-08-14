package org.jithin.payment.application.port.out;

import org.jithin.common.OrderCreatedEvent;
import org.jithin.payment.domain.model.Payment;

import java.util.List;

public interface PaymentEventPublisherPort {

    void publishPaymentProcessedEvent(Payment payment, List<OrderCreatedEvent.OrderItemData> itemData);
}
