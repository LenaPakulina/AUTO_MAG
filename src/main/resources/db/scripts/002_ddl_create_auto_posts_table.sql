CREATE TABLE auto_posts
(
    id SERIAL PRIMARY KEY,
    description text NOT NULL,
    created timestamp,
    auto_user_id int REFERENCES auto_users(id)
);