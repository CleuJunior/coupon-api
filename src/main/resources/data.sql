INSERT INTO coupons (code, description, discount_value, expiration_date, published, deleted_at) VALUES
    ('PROMO1', 'Promoção de lançamento', 10.00, DATEADD('DAY', 30, CURRENT_DATE), true, NULL),
    ('BLACKF', 'Black Friday', 25.50, DATEADD('DAY', 60, CURRENT_DATE), true, NULL),
    ('NATAL1', 'Promoção de Natal', 15.00, DATEADD('DAY', 90, CURRENT_DATE), false, NULL),
    ('VERAO2', 'Promoção de verão', 5.00, DATEADD('DAY', 45, CURRENT_DATE), true, NULL),
    ('FRETE0', 'Frete grátis', 0.50, DATEADD('DAY', 15, CURRENT_DATE), true, NULL),
    ('CLIENT', 'Cliente fiel', 20.00, DATEADD('DAY', 120, CURRENT_DATE), false, NULL),
    ('ANIVER', 'Aniversário da loja', 30.00, DATEADD('DAY', 10, CURRENT_DATE), true, NULL),
    ('EXPIRA', 'Cupom quase expirando', 2.00, DATEADD('DAY', 1, CURRENT_DATE), true, NULL),
    ('DELET1', 'Cupom de exemplo já deletado', 12.00, DATEADD('DAY', 20, CURRENT_DATE), true, CURRENT_TIMESTAMP),
    ('DELET2', 'Cupom de exemplo já deletado', 8.00, DATEADD('DAY', 25, CURRENT_DATE), false, CURRENT_TIMESTAMP);
