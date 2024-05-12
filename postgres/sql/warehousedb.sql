CREATE TABLE books
(
    id                 SERIAL PRIMARY KEY,
    title              VARCHAR(255)     NOT NULL,
    author             VARCHAR(255)     NOT NULL,
    description        TEXT             NOT NULL,
    isbn               VARCHAR(20)      NOT NULL,
    price              DOUBLE PRECISION NOT NULL,
    quantity_available INTEGER          NOT NULL,
    quantity_on_hold   INTEGER          NOT NULL
);

INSERT INTO books (title, author, description, isbn, price, quantity_available, quantity_on_hold)
VALUES ('Book 1', 'Author 1', 'Description for Book 1', 'ISBN-001', 19.99, 100, 0),
       ('Book 2', 'Author 2', 'Description for Book 2', 'ISBN-002', 24.99, 75, 0),
       ('Book 3', 'Author 3', 'Description for Book 3', 'ISBN-003', 29.99, 50, 0),
       ('Book 4', 'Author 4', 'Description for Book 4', 'ISBN-004', 14.99, 120, 0),
       ('Book 5', 'Author 5', 'Description for Book 5', 'ISBN-005', 39.99, 80, 0),
       ('Book 6', 'Author 6', 'Description for Book 6', 'ISBN-006', 9.99, 200, 0),
       ('Book 7', 'Author 7', 'Description for Book 7', 'ISBN-007', 34.99, 60, 0),
       ('Book 8', 'Author 8', 'Description for Book 8', 'ISBN-008', 19.99, 90, 0),
       ('Book 9', 'Author 9', 'Description for Book 9', 'ISBN-009', 44.99, 110, 0),
       ('Book 10', 'Author 10', 'Description for Book 10', 'ISBN-010', 29.99, 150, 0);