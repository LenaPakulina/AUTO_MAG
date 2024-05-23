CREATE TABLE participates
(
   id serial PRIMARY KEY,
   user_id int not null REFERENCES auto_users(id),
   post_id int not null REFERENCES auto_posts(id),
   UNIQUE (user_id, post_id)
);