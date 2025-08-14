CREATE TABLE payments(
 id BIGSERIAL PRIMARY KEY,
 order_id BIGINT NOT NULL UNIQUE,
 amount DECIMAL(10,2) NOT NULL,
 status VARCHAR(50) NOT NULL,
 transaction_id VARCHAR(255),
 payment_date TIMESTAMP WITH TIME ZONE NOT NULL,
 version INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_payments_order_id ON payments(order_id);