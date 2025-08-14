package org.jithin.order.infrastructure.adapter.out.event;

import lombok.extern.slf4j.Slf4j;
import org.jithin.common.OrderCreatedEvent;
import org.jithin.order.application.out.OrderEventPublisherPort;
import org.jithin.order.domain.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrderEventPublisherAdapter  implements OrderEventPublisherPort {
    private static final String ORDER_CREATED_TOPIC ="order.create";
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventPublisherAdapter(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreatedEvent(Order order) {

        OrderCreatedEvent orderCreatedEvent = convertToOrderCreateEvent(order);

        try {
            kafkaTemplate.send(ORDER_CREATED_TOPIC,String.valueOf(order.getId()),orderCreatedEvent);
            log.info("Successfully send event of order {} ",order.getId());
        }catch (Exception e)
        {
            log.info("Failed to publish event order of {} with {} ",order.getId(), e.getMessage());
            throw new RuntimeException("Failed to publish order created event for orderID: "+order.getId(),e);
        }

    }

    private OrderCreatedEvent convertToOrderCreateEvent(Order order) {
        List<OrderCreatedEvent.OrderItemData> itemData = order.getItems().stream()
                .map(item -> new OrderCreatedEvent.OrderItemData(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPricePerUnit()
                )).toList();
        return new OrderCreatedEvent(
                order.getId(),
                order.getCustomerId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                itemData
        );
    }
}

























