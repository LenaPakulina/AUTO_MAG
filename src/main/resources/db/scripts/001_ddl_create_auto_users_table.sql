CREATE TABLE auto_users
(
    id              SERIAL PRIMARY KEY,
    login           VARCHAR unique  NOT NULL,
    password        VARCHAR         NOT NULL,
    user_zone       VARCHAR(128)
);