create table users (
    id serial unique,
    username varchar unique,
    confirmed bool,
    password varchar
);

create table user_roles(
                           id serial unique,
                           role varchar unique
);

insert into user_roles (role) values ('USER'),('ADMIN');

create table user_to_roles(
                              id serial,
                              userId int references users(id),
                              roleId int references user_roles(id)
);


create table conf_tokens(
    id serial unique,
    userId int references users(id),
    token varchar,
    creationDate timestamp,
    confirmationDate timestamp,
    expirationDate timestamp
);

drop table conf_tokens,user_to_roles,user_roles,users;