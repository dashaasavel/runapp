create table if not exists refresh_tokens (
    id serial primary key,
    userId int,
    username varchar,
    token varchar,
    expDate timestamp
);