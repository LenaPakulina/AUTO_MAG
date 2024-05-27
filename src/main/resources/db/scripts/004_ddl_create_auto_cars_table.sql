CREATE TABLE cars
(
   id           serial PRIMARY KEY,
   name         VARCHAR(128) NOT NULL,
   ownershipAt  timestamp WITHOUT TIME ZONE,
   engine_id    int NOT NULL REFERENCES engines(id)
);