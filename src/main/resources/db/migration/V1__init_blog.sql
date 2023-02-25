drop table if exists account;
drop table if exists account_role;
drop table if exists post;
drop table if exists role;
create table account
(
    id         bigint not null auto_increment,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    primary key (id)
);
create table account_role
(
    account_id bigint      not null,
    role_name  varchar(16) not null,
    primary key (account_id, role_name)
);
create table post
(
    id          bigint not null auto_increment,
    body        varchar(255),
    create_time datetime(6),
    title       varchar(255),
    update_time datetime(6),
    account_id  bigint,
    primary key (id)
);
create table role
(
    name varchar(16) not null,
    primary key (name)
);
alter table account_role
    add constraint `role_name_fk_1` foreign key (role_name) references role (name);
alter table account_role
    add constraint `account_id_fk_1` foreign key (account_id) references account (id)
        ON DELETE CASCADE;
alter table post
    add constraint `fk_account` foreign key (account_id) references account (id)
        ON DELETE CASCADE;
