use selling_shoes_website_project;

select * from product_sizes;

create table product_sizes (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    quantity int,
    size int,
    product_id int,
    
    foreign key (product_id) references products (id)
);