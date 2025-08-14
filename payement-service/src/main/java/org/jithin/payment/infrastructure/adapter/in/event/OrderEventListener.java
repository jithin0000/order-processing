package org.jithin.payment.infrastructure.adapter.in.event;

import lombok.extern.slf4j.Slf4j;
import org.jithin.common.OrderCreatedEvent;
import org.jithin.payment.application.port.in.PaymentUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventListener {
    private final PaymentUseCase paymentUseCase;

    public OrderEventListener(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    @KafkaListener(topics = "order.create",groupId = "payment-service-group")
    public  void handleOrderCreatedEvent(OrderCreatedEvent event)
    {
        log.info("Successfully Received Event from order service for orderId: {}",event.orderId());
        try {
            paymentUseCase.processPayment(event.orderId(), event.totalAmount(), event.items());
        }catch (Exception e)
        {
            log.error("Error processing payment for orderId: {}", event.orderId(), e);
        }

    }
}
