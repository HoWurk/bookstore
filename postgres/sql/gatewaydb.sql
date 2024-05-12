CREATE TABLE jwt_tokens
(
    id              SERIAL PRIMARY KEY,
    token           TEXT         NOT NULL,
    username        VARCHAR(255) NOT NULL,
    expiration_date TIMESTAMP    NOT NULL
);