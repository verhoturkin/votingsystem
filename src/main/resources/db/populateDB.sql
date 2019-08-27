DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User1', 'user1@yandex.ru', 'password'),
       ('User2', 'user2@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100002),
       ('ROLE_USER', 100002),
       ('ROLE_USER', 100001);

INSERT INTO restaurants (name)
VALUES ('McDonalds'),
       ('KFC'),
       ('Burger King');

INSERT INTO dishes (date, name, price, restaurant_id)
VALUES ('2015-05-30', 'Биг Мак', 13501, 100003),
       ('2015-05-30', 'Чикен Премьер', 13990, 100003),
       ('2015-05-31', 'Биг Тейсти', 24980,100003),
       ('2015-05-30', 'Биг Кинг', 15010, 100004),
       ('2015-05-30', 'Воппер', 19630, 100004),
       ('2015-05-31', 'Беконайзер', 33058,100004),
       ('2015-05-30', 'БоксМастер', 19425, 100005),
       ('2015-05-30', 'Крылья острые', 10930, 100005),
       ('2015-05-31', 'Баскет S', 38910,100005);

INSERT INTO votes (restaurant_id, user_id, date)
VALUES (100003, 100000, '2015-05-30'),
       (100004, 100001, '2015-05-30'),
       (100005, 100002, '2015-05-31');