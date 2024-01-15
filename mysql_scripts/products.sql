use selling_shoes_website_project;

select 	* from products;

ALTER TABLE products AUTO_INCREMENT=13;
delete from products where id > 0;

ALTER TABLE products ADD COLUMN image_data longblob;


INSERT INTO products (
    created_at, 
    updated_at, 
    description, 
    is_delete, 
    name, 
    price, 
    status, 
    version_name, 
    brand_id, 
    category_id
) VALUES (
    NOW(),  -- current date and time
    NOW(),  -- current date and time
    'This is a stylish and comfortable shoe.',  -- description
    0,  -- is_delete
    'Stylish Shoe',  -- name
    100,  -- price
    'Available',  -- status
    '1.0',  -- version_name
    1,  -- brand_id
    1   -- category_id
);


create table products(
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    description varchar(500),
    is_delete bit(1),
    name varchar(100),
    price int,
    status varchar(50),
    version_name varchar(50),
    brand_id int,
    category_id int,
    
    foreign key (brand_id) references brands (id),
    foreign key (category_id) references categories (id)
);