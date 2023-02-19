insert into role (name) values ('ROLE_USER');
insert into role (name)
values ('ROLE_ADMIN');
insert into account (email, first_name, last_name, password)
values ('user1@mail.com', 'user1', 'user1', 'password');
insert into account_role (account_id, role_name)
values (1, 'ROLE_USER');
insert into account (email, first_name, last_name, password)
values ('user2@mail.com', 'user2', 'user2', 'password');
insert into account_role (account_id, role_name)
values (2, 'ROLE_USER');
insert into account (email, first_name, last_name, password)
values ('admin1@mail.com', 'admin1', 'admin1', 'password');
insert into account_role (account_id, role_name)
values (3, 'ROLE_USER');
insert into account_role (account_id, role_name)
values (3, 'ROLE_ADMIN');
insert into post (account_id, body, create_time, title, update_time)
values (1, 'Body of post1', CURRENT_TIMESTAMP(), 'post1 titile', CURRENT_TIMESTAMP());
insert into post (account_id, body, create_time, title, update_time)
values (2, 'Body of post2', CURRENT_TIMESTAMP(), 'post2 titile', CURRENT_TIMESTAMP());