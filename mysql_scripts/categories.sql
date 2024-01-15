use selling_shoes_website_project;

select * from categories;

INSERT INTO categories (created_at, updated_at, name) VALUES
(NOW(), NOW(), 'Men Shoes'),
(NOW(), NOW(), 'Women Shoes');


create table categories (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    name varchar(50)
);