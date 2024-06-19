CREATE TABLE movie
(
    id           SERIAL PRIMARY KEY,
    title        TEXT NOT NULL,
    views        INT,
    release_date DATE
);

CREATE TABLE filter
(
    id   SERIAL PRIMARY KEY,
    uuid UUID UNIQUE NOT NULL
);

CREATE INDEX idx_filter_uuid ON filter (uuid);


CREATE TABLE criterion
(
    id           SERIAL PRIMARY KEY,
    type         TEXT   NOT NULL,
    operator     TEXT   NOT NULL,
    target_value TEXT   NOT NULL,
    target_field TEXT   NOT NULL,
    filter_id    SERIAL,
    FOREIGN KEY (filter_id) REFERENCES filter (id) ON DELETE CASCADE
);