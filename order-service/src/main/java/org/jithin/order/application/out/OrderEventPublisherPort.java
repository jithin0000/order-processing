package org.jithin.order.application.out;

import org.jithin.order.domain.model.Order;

public interface OrderEventPublisherPort {

    void publishOrderCreatedEvent(Order order);
}
