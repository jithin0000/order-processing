package org.jithin.catalogue.infrastructure.adapter.in.event;

import lombok.extern.slf4j.Slf4j;
import org.jithin.catalogue.application.port.in.ProductUseCase;
import org.jithin.common.PaymentProcessedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class PaymentEventListener {
    private final ProductUseCase productUseCase;

    public PaymentEventListener(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @KafkaListener(topics = "payment.processed",groupId = "catalogue-service-group")
    public void handlePaymentProcessed(PaymentProcessedEvent event)
    {
        if ("SUCCESS".equalsIgnoreCase(event.status().toString())) {
            log.info("Received successful PaymentProcessedEvent for orderId: {}. Updating stock.", event.orderId());

            try {
                // Map the event's item data to the format our use case expects.
                var itemsToUpdate = event.items().stream()
                        .map(item -> new ProductUseCase.ItemStockUpdate(item.productId(), item.quantity()))
                        .collect(Collectors.toList());

                // Call the use case to perform the stock update.
                productUseCase.processStockUpdate(itemsToUpdate);

            } catch (Exception e) {
                log.error("Error processing stock update for orderId: {}", event.orderId(), e);
                // Handle with a DLQ or other compensating action in a real system.
            }
        } else {
            log.info("Ignoring failed payment event for orderId: {}", event.orderId());
        }
    }
}




























