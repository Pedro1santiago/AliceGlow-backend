-- Improves performance for product listing/search when the dataset grows.
-- Indexes support case-insensitive name search and common (active + name) filtering/sorting.

CREATE INDEX IF NOT EXISTS idx_products_name_lower ON products (LOWER(name));

CREATE INDEX IF NOT EXISTS idx_products_active_name_lower ON products (active, LOWER(name));
