CREATE TABLE IF NOT EXISTS users
(
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS books (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    isbn VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    quantity_available INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      order_date TIMESTAMP NOT NULL,
                                      FOREIGN KEY (user_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           book_id BIGINT NOT NULL,
                                           quantity INT NOT NULL,
                                           FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
    );

CREATE TABLE IF NOT EXISTS payments (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        order_id BIGINT NOT NULL,
                                        payment_date TIMESTAMP,
                                        amount DOUBLE NOT NULL,
                                        payment_method VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id)
    );
