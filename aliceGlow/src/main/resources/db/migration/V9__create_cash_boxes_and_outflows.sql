CREATE TABLE cash_boxes (
    id BIGSERIAL PRIMARY KEY,
    business_date DATE NOT NULL UNIQUE,
    balance DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE cash_outflows (
    id BIGSERIAL PRIMARY KEY,
    cash_box_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_cash_outflows_cash_box
        FOREIGN KEY (cash_box_id)
        REFERENCES cash_boxes(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_cash_outflows_occurred_at ON cash_outflows(occurred_at);
