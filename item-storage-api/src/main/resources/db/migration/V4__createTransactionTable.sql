create extension if not exists "pgcrypto";

create table sell_transactions (
    id bigserial not null primary key,
    created_at timestamp not null default current_timestamp
);
