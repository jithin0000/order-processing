package org.jithin.payment.infrastructure.adapter.out.event;

import lombok.extern.slf4j.Slf4j;
import org.jithin.common.OrderCreatedEvent;
import org.jithin.common.PaymentProcessedEvent;
import org.jithin.payment.application.port.out.PaymentEventPublisherPort;
import org.jithin.payment.domain.model.Payment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentEventPublisherAdapter implements PaymentEventPublisherPort {
    public static final String PAYMENT_PROCESSED_TOPIC="payment.processed";
    private final KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;

    public PaymentEventPublisherAdapter(KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishPaymentProcessedEvent(Payment payment, List<OrderCreatedEvent.OrderItemData> itemData) {

        PaymentProcessedEvent paymentEvent = new PaymentProcessedEvent(
                payment.getOrderId(), payment.getAmount(), payment.getStatus(),
                payment.getTransactionId(),
                itemData
        );

        try{

            kafkaTemplate.send(PAYMENT_PROCESSED_TOPIC, String.valueOf(payment.getOrderId()),paymentEvent);

        }catch (Exception e)
        {
            log.info("Failed to publish PaymentProcessedEvent for orderId: {} ",payment.getOrderId());
            throw new RuntimeException("Failed to publish payment processed event: ",e);
        }

    }
}


























