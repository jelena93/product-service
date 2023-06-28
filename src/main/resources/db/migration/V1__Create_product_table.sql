CREATE TABLE product
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255)   NOT NULL UNIQUE,
    price      NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);
