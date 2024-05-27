CREATE TABLE owners
(
   id           serial PRIMARY KEY,
   name         VARCHAR(128) NOT NULL,
   user_id      int REFERENCES auto_users(id)
);
