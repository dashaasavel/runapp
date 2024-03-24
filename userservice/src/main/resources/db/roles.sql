create table user_roles(
    id serial unique,
    role varchar unique
);

drop table user_roles;
insert into user_roles (role) values ('USER'),('ADMIN');

create table user_to_roles(
    id serial,
    userId int references users(id),
    roleId int references user_roles(id)
);

