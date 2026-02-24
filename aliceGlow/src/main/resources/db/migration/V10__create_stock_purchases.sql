CREATE TABLE stock_purchases (
    id BIGSERIAL PRIMARY KEY,
    purchase_date DATE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_stock_purchases_purchase_date ON stock_purchases(purchase_date);
