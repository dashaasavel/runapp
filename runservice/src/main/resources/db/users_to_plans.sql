-- create table if not exists users_to_plans(
--     id serial primary key,
--     userId int references users(id),
--     planId varchar(25),
--     competitionRunType varchar(30)
-- );

create table if not exists plan_info(
    id serial primary key,
    trainingListId varchar(25),
    userId int references users(id),
    competitionRunType varchar(30),
    competitionDate date,
    daysOfWeek text[],
    longRunDistance int
)

-- drop table users_to_plans;