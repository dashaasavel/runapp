create table if not exists user_roles(
    id serial primary key,
    role varchar(30) unique
);

create table if not exists users_to_roles(
    id serial primary key,
    userId int references users(id),
    roleId int references user_roles(id)
);

