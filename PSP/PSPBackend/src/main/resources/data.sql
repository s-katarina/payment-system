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

