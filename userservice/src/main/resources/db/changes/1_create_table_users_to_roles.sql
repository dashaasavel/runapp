create table if not exists users_to_roles(
    id serial primary key,
    userId int references users(id),
    roleId int references user_roles(id)
);