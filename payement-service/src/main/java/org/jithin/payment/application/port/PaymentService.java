package org.jithin.payment.application.port;

import lombok.extern.slf4j.Slf4j;
import org.jithin.common.OrderCreatedEvent;
import org.jithin.payment.application.port.in.PaymentUseCase;
import org.jithin.payment.application.port.out.PaymentEventPublisherPort;
import org.jithin.payment.application.port.out.PaymentRepositoryPort;
import org.jithin.payment.domain.model.Payment;
import org.jithin.common.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PaymentService implements PaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final PaymentEventPublisherPort paymentEventPublisherPort;

    public PaymentService(PaymentRepositoryPort paymentRepositoryPort, PaymentEventPublisherPort paymentEventPublisherPort) {
        this.paymentRepositoryPort = paymentRepositoryPort;
        this.paymentEventPublisherPort = paymentEventPublisherPort;
    }

    @Override
    @Transactional
    public void processPayment(Long orderId, BigDecimal amount, List<OrderCreatedEvent.OrderItemData> items) {

        log.info("Processing payment for order with id {} for amount {}",orderId,amount);
        boolean paymentSuccessful = amount.compareTo(new BigDecimal("5000")) < 0;

        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .paymentDate(ZonedDateTime.now())
                .build();

        if(paymentSuccessful)
        {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());
            log.info("Successfully completed payment for orderID: {} ", orderId);

        }else{
            payment.setStatus(PaymentStatus.FAILED);
            payment.setTransactionId(UUID.randomUUID().toString());
            log.info("Payment Failed for orderID : {} ", orderId);
        }

        Payment savedPayment = paymentRepositoryPort.save(payment);
        paymentEventPublisherPort.publishPaymentProcessedEvent(savedPayment,items);

    }
}
























