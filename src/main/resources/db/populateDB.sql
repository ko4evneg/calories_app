DELETE FROM MEALS;
DELETE FROM USER_ROLES;
DELETE FROM USERS;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100003;

INSERT INTO MEALS (id, description, calories, date_time, user_id)
VALUES (DEFAULT, 'Breakfast', 500, to_timestamp('29-04-2022:10-00', 'DD-MM-YYYY:HH24-MI'), 100000),
       (DEFAULT, 'Dinner', 1000, to_timestamp('29-04-2022:13-00', 'DD-MM-YYYY:HH24-MI'), 100000),
       (DEFAULT, 'Supper', 500, to_timestamp('29-04-2022:20-00', 'DD-MM-YYYY:HH24-MI'), 100000),
       (DEFAULT, 'Edge case', 100, to_timestamp('30-04-2022:00-00', 'DD-MM-YYYY:HH24-MI'), 100000),
       (DEFAULT, 'Breakfast', 1000, to_timestamp('30-04-2022:10-00', 'DD-MM-YYYY:HH24-MI'), 100000),
       (DEFAULT, 'Dinner', 500, to_timestamp('30-04-2022:13-00', 'DD-MM-YYYY:HH24-MI'), 100000),
       (DEFAULT, 'Supper', 410, to_timestamp('30-04-2022:20-00', 'DD-MM-YYYY:HH24-MI'), 100000),
       (DEFAULT, 'Admin lunch', 510, to_timestamp('01-05-2022:14-00', 'DD-MM-YYYY:HH24-MI'), 100001),
       (DEFAULT, 'Admin supper', 1500, to_timestamp('01-05-2022:21-00', 'DD-MM-YYYY:HH24-MI'), 100001);