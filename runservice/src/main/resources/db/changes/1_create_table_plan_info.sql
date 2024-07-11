create table if not exists plan_info(
    id serial primary key,
    trainingsId varchar(25),
    userId int,
    competitionRunType varchar(30),
    competitionDate date,
    daysOfWeek text[],
    longRunDistance int
);