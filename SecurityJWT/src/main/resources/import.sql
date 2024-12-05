-- init data
INSERT INTO products (id,price, stock, version, category, description, name) VALUES(1,3.12, 10, 0, 'sport', 'raqueta azul', 'raqueta q-max');
INSERT INTO products (id,price, stock, version, category, description, name) VALUES(2,123.12, 2, 0, 'sport', 'boxeo', 'guantes q-max');
INSERT INTO products (id,price, stock, version, category, description, name) VALUES(3,3.12, 0, 0, 'sport', 'pelota playa', 'pelota q-max');
INSERT INTO products (id,price, stock, version, category, description, name) VALUES(4,1, 40, 0, 'eat', 'pan cristal 4ud', 'pack pan cristal');
INSERT INTO products (id,price, stock, version, category, description, name) VALUES(5,3.12, 10, 0, 'short', 'camiseta roja', 'camiseta q-max');

INSERT INTO PERMISSIONS (name,description,created_at) VALUES ('READ','READ_PERMISSION',now());
INSERT INTO PERMISSIONS (name,description,created_at) VALUES ('WRITE','WRITE_PERMISSION',now());
INSERT INTO PERMISSIONS (name,description,created_at) VALUES ('DELETE','DELETE_PERMISSION',now());

INSERT INTO ROLES (name,description,created_at) VALUES ('USER','ROLE_USER',now());
INSERT INTO ROLES (name,description,created_at) VALUES ('ADMIN','ROLE_ADMIN',now());
INSERT INTO ROLES (name,description,created_at) VALUES ('MODERATOR','ROLE_MODERATOR',now());

INSERT INTO ROLES_PERMISSIONS (permission_id,role_id) values (1,1);
INSERT INTO ROLES_PERMISSIONS (permission_id,role_id) values (2,1),(2,2),(2,3);

-- pwd= user
INSERT INTO USERS (USERNAME ,PASSWORD ,FIRST_NAME ,LAST_NAME ,EMAIL ,BIRTHDAY ,CREATED_AT ,UPDATED_AT ) VALUES ('user','$2a$10$aOH7D6kU5Q/8.bzWrNY6AOnbLbMUTzA47CRkRfNmFxelEdhqi1RxS','andres','ruiz','user@bla.com','1992-07-06',now(),now());

-- pwd= admin
INSERT INTO USERS (USERNAME ,PASSWORD ,FIRST_NAME ,LAST_NAME ,EMAIL ,BIRTHDAY ,CREATED_AT ,UPDATED_AT ) VALUES ('admin','$2a$10$6Q6x57YKczY.7I7lyacP6e6RwmjhizgR2icGk9heGsBHPMppHxb/K','andres','ruiz','admin@bla.com','1992-07-06',now(),now())

INSERT INTO USERS_ROLES (user_id,role_id) VALUES (1,1);
INSERT INTO USERS_ROLES (user_id,role_id) VALUES (2,2);