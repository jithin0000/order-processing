CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL,
    version INT NOT NULL DEFAULT 0
);

INSERT INTO products (name, description, price, stock_quantity) VALUES
('Laptop Pro', 'A powerful laptop for professionals', 1499.99, 50),
('Wireless Mouse', 'Ergonomic wireless mouse', 75.50, 200),
('Mechanical Keyboard', 'A keyboard for typing enthusiasts', 120.00, 100);
