CREATE TABLE IF NOT EXISTS events (
    id          SERIAL PRIMARY KEY,
    event_type  VARCHAR(255) NOT NULL,
    user_id     VARCHAR(255),
    timestamp   TIMESTAMP,
    metadata    TEXT
);
