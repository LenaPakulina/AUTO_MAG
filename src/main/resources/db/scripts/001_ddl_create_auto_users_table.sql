CREATE TABLE auto_users
(
    id SERIAL PRIMARY KEY,
    login varchar unique NOT NULL,
    password varchar     NOT NULL
);