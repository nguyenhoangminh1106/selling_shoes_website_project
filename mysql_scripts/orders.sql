use selling_shoes_website_project;

select * from orders;

ALTER TABLE orders AUTO_INCREMENT=1;
delete from orders where id > 0;

INSERT INTO orders (
    created_at,
    updated_at,
    consignee,
    consignee_phone,
    delivery_address,
    delivery_status,
    payment_method,
    payment_status,
    sent_mail,
    total_amount,
    customer_id
)
VALUES (
    NOW(), -- current date and time
    NOW(), -- current date and time
    'John Doe', -- consignee
    '1234567890', -- consignee phone
    '123 Main St, Anytown, USA', -- delivery address
    'Processing', -- delivery status
    'ATM', -- payment method
    'Paid', -- payment status
    0, -- sent mail
    100, -- total amount
    1 -- customer id
);

create table orders (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    consignee varchar(100),
    consignee_phone varchar(20),
    delivery_address varchar(200),
    delivery_status varchar(50),
    payment_method varchar(50),
    payment_status varchar(50),
    sent_mail bit(1),
	total_amount int,
    customer_id int,
    
    foreign key (customer_id) references users (id)
);