use selling_shoes_website_project;

select * from order_details;

ALTER TABLE order_details AUTO_INCREMENT=1;
delete from order_details where id > 0;

drop table order_details;


create table order_details (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    price int,
    quantity int,
    total_amount int,
    order_id int,
    product_id int,
    
    foreign key (order_id) references orders(id),
    foreign key (product_id) references products (id)
);