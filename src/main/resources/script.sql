DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id BIGINT PRIMARY KEY ,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE transactions(
    id BIGINT PRIMARY KEY ,
    customer_id BIGINT NOT NULL ,
    amount DECIMAL(10,2) NOT NULL ,
    txn_date DATE NOT NULL ,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

insert into customers (id, name) values
(1, 'Alice'),
(2, 'Bob'),
(3, 'Charlie'),
(4, 'dummy');


insert into transactions (id, customer_id, amount, txn_date) VALUES
(1001, 1, 120.00,  date '2025-01-05'),
(1002, 1, 75.00, date '2025-01-15'),
(1008, 1, 200.00, date '2025-02-01'),
(1009, 1, 145.00, date '2025-03-05'),
(1003, 2, 50.19, date '2025-01-06' ),
(1004, 2, 110.00, date '2025-01-07'),
(1005, 3, 190.00, date '2025-01-08');



