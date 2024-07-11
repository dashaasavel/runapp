create table if not exists user_roles(
    id serial primary key,
    role varchar(30) unique
);

