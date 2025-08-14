package org.jithin.payment.application.port.out;

import org.jithin.payment.domain.model.Payment;

public interface PaymentRepositoryPort {

    Payment save(Payment payment);
}
