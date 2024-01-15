use selling_shoes_website_project;

select * from brands;

ALTER TABLE brands AUTO_INCREMENT=7;
delete from brands where id = 7;

INSERT INTO brands (
    created_at, 
    updated_at, 
    name
) VALUES (
    NOW(),  -- current date and time
    NOW(),  -- current date and time
    'Nike'  -- name
);


create table brands (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    name varchar(50)
);