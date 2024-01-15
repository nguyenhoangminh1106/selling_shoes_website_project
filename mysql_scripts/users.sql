use selling_shoes_website_project;

select * from users;

insert into users (email, password, role) values ('khuongtunha1106@gmail.com', 123, 'admin');
ALTER TABLE users AUTO_INCREMENT=3;
delete from users where id = 3;

update users set phone = '0912345678' where id = 1;

create table users (
	id int primary key auto_increment,
    created_at datetime,
    updated_at datetime,
    address varchar(200),
    date_of_birth date,
    email varchar(100),
    gender bit(1),
    image_data longblob,
    image_path varchar(500),
    is_delete bit(1),
    name varchar(100),
    password varchar(200),
    phone varchar(20),
    role varchar(50)
);