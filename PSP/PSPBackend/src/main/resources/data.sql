-- Sample merchant data for testing
-- Note: Merchant passwords are now hashed using BCrypt in the application
-- This plain text password will be automatically hashed on first save if using JPA DDL auto-update
INSERT INTO merchants (merchant_id, merchant_name, merchant_password, currency, success_url, fail_url, error_url, active)
VALUES 
    ('webshop-merchant-1', 'WebShop Merchant 1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USD', 'http://localhost:3000/payment/success', 'http://localhost:3000/payment/fail', 'http://localhost:3000/payment/error', true)
ON CONFLICT (merchant_id) DO NOTHING;
-- The BCrypt hash above corresponds to password: 'merchant-password-123'

-- Payment method data
-- Using fixed UUID to prevent duplicates on restart
INSERT INTO payment_methods (id, name, service_name, service_url)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000', 'Bank Payment', 'BankBackend', 'http://localhost:8082')
ON CONFLICT (id) DO NOTHING;

-- Admin user data
-- Note: Admin user will be created by DataInitializer with proper password hash
-- Default credentials: username='admin', password='admin123'
-- If you want to create it manually via SQL, use DataInitializer to generate the hash first

