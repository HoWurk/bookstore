CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    role     VARCHAR(20)  NOT NULL
);

INSERT INTO users (username, password, email, role)
VALUES ('john_doe', '$2a$10$FOP027c7hdwbJxlhjhJL2eza04Ty/cPG1m0DqmNwoGSra/4fP8UTG', 'john@example.com', 'ROLE_USER'),
       ('jane_smith', '$2a$10$FOP027c7hdwbJxlhjhJL2eza04Ty/cPG1m0DqmNwoGSra/4fP8UTG', 'jane@example.com', 'ROLE_ADMIN'),
       ('bob_marley', '$2a$10$FOP027c7hdwbJxlhjhJL2eza04Ty/cPG1m0DqmNwoGSra/4fP8UTG', 'bob@example.com', 'ROLE_USER');
