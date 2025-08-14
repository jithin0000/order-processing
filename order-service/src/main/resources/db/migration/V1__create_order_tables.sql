-- This script creates the tables for the order service.

-- The 'orders' table will store the main information for each order.
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL, -- Assuming a simple customer ID for now
    order_date TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL, -- e.g., PENDING, COMPLETED, FAILED
    total_amount DECIMAL(10, 2) NOT NULL,
    version INT NOT NULL DEFAULT 0
);

-- The 'order_items' table will store the individual products within each order.
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_per_unit DECIMAL(10, 2) NOT NULL
);

-- Add an index on orders.id for faster lookups from order_items
CREATE INDEX idx_order_items_order_id ON order_items(order_id);

