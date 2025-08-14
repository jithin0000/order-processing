package org.jithin.order.infrastructure.adapter.in.web;


import org.jithin.order.application.in.OrderUseCase;
import org.jithin.order.domain.model.Order;
import org.jithin.order.domain.model.OrderItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        // 1. Map the request DTO to our internal domain model.
        List<OrderItem> domainItems = request.items().stream()
                .map(itemReq -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(itemReq.productId());
                    item.setQuantity(itemReq.quantity());
                    item.setPricePerUnit(itemReq.pricePerUnit());
                    return item;
                })
                .collect(Collectors.toList());

        // 2. Call the use case to execute the business logic.
        Order createdOrder = orderUseCase.createOrder(request.customerId(), domainItems);

        // 3. Map the resulting domain object to our response DTO.
        OrderResponse response = OrderResponse.fromDomain(createdOrder);

        // 4. Return a 201 Created response.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}