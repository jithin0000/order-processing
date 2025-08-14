package org.jithin.order.application.out;

import org.jithin.order.domain.model.Order;

import java.util.Optional;

public interface OrderRepositoryPort {

    Order saveOrder(Order order);
    Optional<Order> findById(Long orderId);
}
