CREATE TABLE jwt_tokens
(
    id              SERIAL PRIMARY KEY,
    token           TEXT UNIQUE NOT NULL,
    username        VARCHAR(50) NOT NULL,
    expiration_date TIMESTAMP   NOT NULL
);