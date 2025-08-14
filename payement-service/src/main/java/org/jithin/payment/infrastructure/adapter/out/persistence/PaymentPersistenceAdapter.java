package org.jithin.payment.infrastructure.adapter.out.persistence;

import org.jithin.payment.application.port.out.PaymentRepositoryPort;
import org.jithin.payment.domain.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceAdapter implements PaymentRepositoryPort {
    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentPersistenceAdapter(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
