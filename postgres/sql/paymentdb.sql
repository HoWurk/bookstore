CREATE TABLE payments
(
    id             SERIAL PRIMARY KEY,
    order_id       BIGINT           NOT NULL,
    payment_date   TIMESTAMP,
    amount         DOUBLE PRECISION NOT NULL,
    payment_method VARCHAR(50),
    status         VARCHAR(50)      NOT NULL
);

INSERT INTO payments (order_id, payment_date, amount, payment_method, status)
VALUES (1, '2024-03-16 10:30:00', 59.97, 'Credit Card', 'PAID'),
       (2, '2024-03-17 09:45:00', 119.97, 'PayPal', 'PAID'),
       (3, '2024-03-18 11:50:00', 89.97, 'Credit Card', 'PAID'),
       (4, '2024-03-18 13:30:00', 74.97, 'PayPal', 'PAID'),
       (5, '2024-03-19 08:25:00', 119.97, 'Credit Card', 'PAID'),
       (6, '2024-03-20 15:30:00', 29.99, 'Credit Card', 'PAID'),
       (7, NULL, 69.98, 'Credit Card', 'NOT PAID'),
       (8, NULL, 89.97, 'PayPal', 'NOT PAID'),
       (9, NULL, 134.97, NULL, 'NOT PAID'),
       (10, NULL, 59.98, NULL, 'NOT PAID');