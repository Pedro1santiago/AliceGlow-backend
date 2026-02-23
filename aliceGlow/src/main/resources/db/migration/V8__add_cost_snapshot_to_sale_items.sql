ALTER TABLE sale_items
ADD COLUMN unit_cost_price DECIMAL(10,2),
ADD COLUMN cost_subtotal DECIMAL(10,2);

UPDATE sale_items si
SET unit_cost_price = p.cost_price,
    cost_subtotal = (p.cost_price * si.quantity)
FROM products p
WHERE si.product_id = p.id
  AND si.unit_cost_price IS NULL;

ALTER TABLE sale_items
ALTER COLUMN unit_cost_price SET NOT NULL,
ALTER COLUMN cost_subtotal SET NOT NULL;
