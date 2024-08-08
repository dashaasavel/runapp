create table if not exists refresh_tokens (
    id serial primary key,
    userId int references users(id),
    username varchar references users(username),
    token varchar,
    expDate timestamp
);