package org.jithin.order.infrastructure.adapter.in.event;

import lombok.extern.slf4j.Slf4j;
import org.jithin.common.PaymentProcessedEvent;
import org.jithin.order.application.in.OrderUseCase;
import org.jithin.order.domain.model.OrderStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEventListener {
    private final OrderUseCase orderUseCase;

    public PaymentEventListener(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @KafkaListener(topics = "payment.processed",groupId = "order-service-group")
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event)
    {
        log.info("Received PaymentProcessedEvent for orderId: {} with status: {}", event.orderId(), event.status());
        try {
            OrderStatus newStatus = "SUCCESS".equalsIgnoreCase(event.status().toString())
                    ? OrderStatus.COMPLETED : OrderStatus.FAILED;
            orderUseCase.updateOrderStatus(event.orderId(),newStatus);

        }catch (Exception e)
        {
            log.error("Error updating status for orderId: {}", event.orderId(), e);


        }

    }
}
