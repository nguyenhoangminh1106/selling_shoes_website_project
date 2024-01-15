use selling_shoes_website_project;

select * from wishlist;

ALTER TABLE wishlist AUTO_INCREMENT=1;

delete from wishlist where id > 0;

drop table wishlist;

create table wishlist (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    customer_id int,
    product_id int,
    
    foreign key (customer_id) references users (id),
    foreign key (product_id) references products (id)
);