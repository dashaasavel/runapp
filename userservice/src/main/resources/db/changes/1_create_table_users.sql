create table if not exists users (
    id serial primary key,
    firstName varchar,
    username varchar unique,
    confirmed bool,
    password varchar
);