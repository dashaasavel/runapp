create table if not exists users (
    id serial primary key,
    username varchar unique,
    confirmed bool,
    password varchar
);