CREATE TABLE history_owners
(
   id           serial PRIMARY KEY,
   car_id       int NOT NULL REFERENCES cars(id),
   owner_id     int NOT NULL REFERENCES owners(id),
   startAt      timestamp WITHOUT TIME ZONE,
   endAt        timestamp WITHOUT TIME ZONE
);
