CREATE TABLE orders
(
    id         SERIAL PRIMARY KEY,
    user_id    BIGINT    NOT NULL,
    order_date TIMESTAMP NOT NULL
);


CREATE TABLE order_items
(
    id       SERIAL PRIMARY KEY,
    order_id BIGINT  NOT NULL,
    book_id  BIGINT  NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

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