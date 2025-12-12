-- Sample merchant data for testing
-- Note: Merchant passwords are now hashed using BCrypt in the application
-- This plain text password will be automatically hashed on first save if using JPA DDL auto-update
INSERT INTO merchants (merchant_id, merchant_name, merchant_password, currency, success_url, fail_url, error_url, active)
VALUES 
    ('webshop-merchant-1', 'WebShop Merchant 1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USD', 'http://localhost:3000/payment/success', 'http://localhost:3000/payment/fail', 'http://localhost:3000/payment/error', true)
ON CONFLICT (merchant_id) DO NOTHING;
-- The BCrypt hash above corresponds to password: 'merchant-password-123'

-- Payment method data
INSERT INTO payment_methods (id, name, service_name, service_url)
VALUES 
    (gen_random_uuid(), 'Bank Payment', 'BankBackend', 'http://localhost:8082')
ON CONFLICT (id) DO NOTHING;

