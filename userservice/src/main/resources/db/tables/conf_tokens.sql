create table if not exists conf_tokens(
    id serial primary key,
    userId int references users(id),
    token varchar,
    creationDate timestamp,
    confirmationDate timestamp,
    expirationDate timestamp
);