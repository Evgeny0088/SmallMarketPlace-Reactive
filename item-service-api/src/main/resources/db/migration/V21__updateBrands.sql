alter table brands
add column updated_at timestamp;
create index index_brand_name on brands(brand_name);
