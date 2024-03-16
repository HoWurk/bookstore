INSERT INTO users (username, password, email, role)
VALUES ('user1', 'password1', 'user1@example.com', 'ROLE_USER'),
       ('user2', 'password2', 'user2@example.com', 'ROLE_USER'),
       ('user3', 'password3', 'user3@example.com', 'ROLE_USER'),
       ('admin1', 'adminpass1', 'admin1@example.com', 'ROLE_ADMIN'),
       ('admin2', 'adminpass2', 'admin2@example.com', 'ROLE_ADMIN');

INSERT INTO books (title, author, description, isbn, price, quantity_available)
VALUES ('Book 1', 'Author 1', 'Description for Book 1', 'ISBN-001', 19.99, 100),
       ('Book 2', 'Author 2', 'Description for Book 2', 'ISBN-002', 24.99, 75),
       ('Book 3', 'Author 3', 'Description for Book 3', 'ISBN-003', 29.99, 50),
       ('Book 4', 'Author 4', 'Description for Book 4', 'ISBN-004', 14.99, 120),
       ('Book 5', 'Author 5', 'Description for Book 5', 'ISBN-005', 39.99, 80),
       ('Book 6', 'Author 6', 'Description for Book 6', 'ISBN-006', 9.99, 200),
       ('Book 7', 'Author 7', 'Description for Book 7', 'ISBN-007', 34.99, 60),
       ('Book 8', 'Author 8', 'Description for Book 8', 'ISBN-008', 19.99, 90),
       ('Book 9', 'Author 9', 'Description for Book 9', 'ISBN-009', 44.99, 110),
       ('Book 10', 'Author 10', 'Description for Book 10', 'ISBN-010', 29.99, 150);

INSERT INTO orders (user_id, order_date)
VALUES (1, '2024-03-16T10:00:00'),
       (1, '2024-03-17T09:30:00'),
       (2, '2024-03-18T11:45:00'),
       (2, '2024-03-18T13:15:00'),
       (3, '2024-03-19T08:20:00'),
       (3, '2024-03-20T15:00:00'),
       (4, '2024-03-21T12:30:00'),
       (4, '2024-03-21T14:45:00'),
       (5, '2024-03-22T09:00:00'),
       (5, '2024-03-23T10:30:00');

INSERT INTO order_items (order_id, book_id, quantity)
VALUES (1, 1, 2),
       (1, 3, 1),
       (2, 5, 3),
       (3, 2, 1),
       (4, 4, 2),
       (5, 6, 1),
       (6, 8, 4),
       (7, 10, 2),
       (8, 7, 1),
       (9, 9, 3);

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
