package org.jithin.order.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jithin.order.application.in.OrderUseCase;
import org.jithin.order.application.out.OrderEventPublisherPort;
import org.jithin.order.application.out.OrderRepositoryPort;
import org.jithin.order.domain.model.Order;
import org.jithin.order.domain.model.OrderItem;
import org.jithin.order.domain.model.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService implements OrderUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderEventPublisherPort orderEventPublisherPort;

    public OrderService(OrderRepositoryPort orderRepositoryPort, OrderEventPublisherPort orderEventPublisherPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderEventPublisherPort = orderEventPublisherPort;
    }


    @Transactional
    @Override
    public Order createOrder(String customerId, List<OrderItem> orderItems) {

        Order order = Order.builder()
                .customerId(customerId)
                .orderDate(ZonedDateTime.now())
                .status(OrderStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItem item : orderItems)
        {
            totalAmount = totalAmount.add(
                    item.getPricePerUnit().multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    )
            );
            order.addItem(item);
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepositoryPort.saveOrder(order);
        orderEventPublisherPort.publishOrderCreatedEvent(savedOrder);
        return savedOrder;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {

        log.info("Updating order status of orderId {} to {} ",orderId, status);
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("no order with this id " + orderId));

        if(order.getStatus() == OrderStatus.PENDING)
        {
            order.setStatus(status);
            return orderRepositoryPort.saveOrder(order);
        }else {
            log.warn("Order {} is not in PENDING state. Current state: {}. No update performed.", orderId, order.getStatus());
            return order;
        }
    }
}


























