create extension if not exists "pgcrypto";

create table brands (
    id bigserial not null primary key,
    owner_id varchar(255) not null,
    brand_name varchar(255) not null,
    created_at timestamp not null default current_timestamp,
    constraint unique_bname unique (brand_name)
);

create table items(
    id bigserial not null primary key,
    brand_id bigint not null,
    parent_item bigint,
    item_type varchar(255) not null,
    created_at timestamp not null default current_timestamp,
    constraint brandId_id_fk foreign key (brand_id) references brands (id),
    constraint parent_item_fk foreign key (parent_item) references items (id)
);
