alter table items
drop constraint brandId_id_fk,
add constraint brandId_id_fk foreign key (brand_id) references brands(id) on delete cascade;
