-- Sample merchant data for testing
-- Note: In production, merchant passwords should be hashed
INSERT INTO merchants (merchant_id, merchant_password, currency, success_url, fail_url, error_url)
VALUES 
    ('webshop-merchant-1', 'merchant-password-123', 'USD', 'http://localhost:3000/payment/success', 'http://localhost:3000/payment/fail', 'http://localhost:3000/payment/error')
ON CONFLICT (merchant_id) DO NOTHING;

