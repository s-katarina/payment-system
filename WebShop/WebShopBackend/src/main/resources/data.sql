-- Enable pgcrypto for UUID generation (run once per database)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Insert 8 packages
INSERT INTO packages (id, package_name, price) VALUES
(gen_random_uuid(), 'Basic Package', 9.99),
(gen_random_uuid(), 'Standard Package', 19.99),
(gen_random_uuid(), 'Premium Package', 29.99),
(gen_random_uuid(), 'Gold Package', 39.99),
(gen_random_uuid(), 'Diamond Package', 59.99),
(gen_random_uuid(), 'Ultimate Package', 79.99);
