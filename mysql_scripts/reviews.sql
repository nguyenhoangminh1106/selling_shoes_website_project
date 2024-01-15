use selling_shoes_website_project;

select * from reviews;

ALTER TABLE reviews AUTO_INCREMENT=1;

delete from reviews where id > 0;

drop table reviews;

create table reviews (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    content varchar(500),
    rating int,
    title varchar(50),
    customer_id int,
    product_id int,
    
    foreign key (customer_id) references users (id),
    foreign key (product_id) references products (id)
);