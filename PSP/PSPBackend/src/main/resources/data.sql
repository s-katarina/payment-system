-- Sample merchant data for testing
-- Note: In production, merchant passwords should be hashed
INSERT INTO merchants (merchant_id, merchant_name, merchant_password, currency, success_url, fail_url, error_url)
VALUES 
    ('webshop-merchant-1', 'WebShop Merchant 1', 'merchant-password-123', 'USD', 'http://localhost:3000/payment/success', 'http://localhost:3000/payment/fail', 'http://localhost:3000/payment/error')
ON CONFLICT (merchant_id) DO NOTHING;

-- Payment method data
INSERT INTO payment_methods (id, name, service_name, service_url)
VALUES 
    (gen_random_uuid(), 'Bank Payment', 'BankBackend', 'http://localhost:8082')
ON CONFLICT (id) DO NOTHING;

