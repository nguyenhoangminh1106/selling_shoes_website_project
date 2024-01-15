use selling_shoes_website_project;

select * from cart_items;

ALTER TABLE cart_items AUTO_INCREMENT=1;

delete from cart_items where id > 0;

drop table cart_items;

create table cart_items (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    quantity int,
    customer_id int,
    product_id int,
    
    foreign key (customer_id) references users (id),
    foreign key (product_id) references products (id)
);