create table  if not exists grpc_metrics(
    id serial primary key,
    timestamp timestamp not null,
    channel varchar(15) not null,
    value bytea
)