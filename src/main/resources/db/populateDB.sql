DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User1', 'user1@yandex.ru', '{bcrypt}$2a$10$AUK/O9NGODoAgrsivmQtq.gxeufkw26DQQKdnt2jD2m5v4QV.X56a'),
       ('Юзер2', 'user2@yandex.ru', '{bcrypt}$2a$10$wjN9oyqcGA4smpPDnRjyBepOM3VB0IMVJHX6gR5HXkwXLJap8spe6'),
       ('Admin', 'admin@gmail.com', '{bcrypt}$2a$10$ZVE6fWfcNd28JUdMjh1tZeOKxEDFMjB6wdEhr8LL1rsPGSh3vHNRO');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100002),
       ('ROLE_USER', 100001);

INSERT INTO restaurants (name)
VALUES ('McDonalds'),
       ('KFC'),
       ('Burger King');

INSERT INTO dishes (date, name, price, restaurant_id)
VALUES ('2015-05-30', 'Биг Мак', 13501, 100003),
       ('2015-05-30', 'Чикен Премьер', 13990, 100003),
       ('2015-05-31', 'Биг Тейсти', 24980,100003),
       ('2015-05-30', 'Биг Кинг', 15010, 100005),
       ('2015-05-30', 'Воппер', 19630, 100005),
       ('2015-05-31', 'Беконайзер', 33058,100005),
       ('2015-05-30', 'БоксМастер', 19425, 100004),
       ('2015-05-30', 'Крылья острые', 10930, 100004),
       ('2015-05-31', 'Баскет S', 38910,100004);

INSERT INTO votes (restaurant_id, user_id, date)
VALUES (100003, 100000, '2015-05-30'),
       (100004, 100001, '2015-05-31');

