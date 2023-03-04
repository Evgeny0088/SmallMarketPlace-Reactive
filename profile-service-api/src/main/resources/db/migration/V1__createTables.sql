create extension if not exists "pgcrypto";

create table if not exists roles (
    id varchar(255) not null primary key default gen_random_uuid(),
    role_name varchar(255) not null,
    constraint unique_role_name unique (role_name)
);

create table if not exists profiles (
    id varchar(255) not null primary key default gen_random_uuid(),
    username varchar(255) not null,
    pass varchar(255) not null,
    email varchar(255) not null,
    roles varchar(4096),
    active boolean not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    email_send_at timestamp not null,
    constraint unique_username unique (username)
);
