package org.jithin.order.infrastructure.adapter.out.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jithin.order.infrastructure.adapter.in.web.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" }, topics = {"order.created"})
class OrderEventPublisherAdapterTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final BlockingQueue<OrderCreatedEvent> receivedEvents = new LinkedBlockingQueue<>();

    @Test
    void shouldPublishOrderCreatedEventWhenOrderIsCreated() throws  Exception{
        var itemRequest = new CreateOrderRequest.OrderItemRequest(1L, 2, new BigDecimal("50.00"));
        var createOrderRequest = new CreateOrderRequest("customer-test-123", Collections.singletonList(itemRequest));

        // 2. Perform the POST request to the controller endpoint
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated());

        // 3. Wait for the event to be received by our test listener
        // We wait for a max of 5 seconds.
        OrderCreatedEvent receivedEvent = receivedEvents.poll(5, TimeUnit.SECONDS);

        // 4. Assert that the received event is not null and has the correct data
        assertThat(receivedEvent).isNotNull();
        assertThat(receivedEvent.customerId()).isEqualTo("customer-test-123");
        assertThat(receivedEvent.totalAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(receivedEvent.items().size()).isEqualTo(1);
        assertThat(receivedEvent.items().get(0).productId()).isEqualTo(1L);
    }


    @Configuration
    static class KafkaTestListener{

        @KafkaListener(topics = "order.created",groupId = "test-group")
        public void consume(OrderCreatedEvent orderCreatedEvent)
        {
            receivedEvents.add(orderCreatedEvent);
        }

    }




}





















