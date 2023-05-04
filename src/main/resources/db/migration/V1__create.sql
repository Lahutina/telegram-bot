drop table if exists users cascade;

create table users (
    user_id int8 not null,
    is_enabled boolean not null,
    user_name varchar(255), primary key (user_id)
);