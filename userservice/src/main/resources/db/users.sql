create table if not exists users (
    id serial primary key,
    username varchar unique,
    confirmed bool,
    password varchar
);

-- create table if not exists user_roles(
--                            id serial primary key,
--                            role varchar unique
-- );

-- insert into user_roles (role) values ('USER'),('ADMIN');

-- create table users_to_roles(
--                               id serial,
--                               userId int references users(id),
--                               roleId int references user_roles(id)
-- );


create table if not exists conf_tokens(
    id serial primary key,
    userId int references users(id),
    token varchar,
    creationDate timestamp,
    confirmationDate timestamp,
    expirationDate timestamp
);

-- drop table conf_tokens,users_to_roles,user_roles,users;