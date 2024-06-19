INSERT INTO movie (id, title, views, release_date)
VALUES (9990, 'Inception', 10, '1910-07-16'),
       (9991, 'The Matrix', 20, '1999-03-31'),
       (9992, 'Interstellar', 30, '2014-11-07');

INSERT INTO filter (id, uuid)
VALUES (9990, 'f47ac10b-58cc-4372-a567-0e02b2c3d479');

INSERT INTO criterion (id, target_field, operator, target_value, type, filter_id)
VALUES (9991, 'title', 'CONTAINS', 'he', 'STRING', 9990),
       (9992, 'views', 'LESS_THAN_OR_EQUAL', '200', 'NUMBER', 9990),
       (9993, 'releaseDate', 'AFTER', '1920-05-16', 'DATE', 9990);