package org.jithin.order.application.in;

import org.jithin.order.domain.model.Order;
import org.jithin.order.domain.model.OrderItem;
import org.jithin.order.domain.model.OrderStatus;

import java.util.List;

public interface OrderUseCase {

    Order createOrder(String customerId, List<OrderItem> orderItems);
    Order updateOrderStatus(Long orderId, OrderStatus status);
}
