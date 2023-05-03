-- passwd = 123456
INSERT INTO users (email,password,name,last_name,phone_number,enabled,created_at,updated_at) 
    VALUES ('jerry@gmail.com','6fd702893abe7973','Gerardo','Torreblanca','55555','1','2023-01-01','2023-01-01');

INSERT INTO roles (name) VALUES ('Admin');
INSERT INTO roles (name) VALUES ('Default');

INSERT INTO users_roles (user_id,role_id) VALUES (1,1);

INSERT INTO catalog_type_event (name) VALUES ('Alquiler');
INSERT INTO catalog_type_event (name) VALUES ('Venta');

INSERT INTO catalog_status_event (name) VALUES ('Apartado');
INSERT INTO catalog_status_event (name) VALUES ('En renta');
INSERT INTO catalog_status_event (name) VALUES ('Pendiente');
INSERT INTO catalog_status_event (name) VALUES ('Cancelado');
INSERT INTO catalog_status_event (name) VALUES ('Finalizado');

INSERT INTO general_info (key_v,value_v) VALUES ("COMPANY_NAME","Porte Formal");
INSERT INTO general_info (key_v,value_v) VALUES ("COMPANY_PHONES","7777");
INSERT INTO general_info (key_v,value_v) VALUES ("COMPANY_ADDRESS","Cuauhtemoc 62. Chilpancingo, Gro.");
INSERT INTO general_info (key_v,value_v) VALUES ("INFO_FOOTER_PDF_A5","Es necesario traer tu INE original y comprobante de domicilio original\nTels. 7474985486,7471067891(solo whatsapp). Horario Lunes a Sabado 10:30 a 19:30.\nConserve esta nota para cualquier aclaración. Se cobrará $50.00 por cada dia sin devolver\n a partir del dia indicado como 'Fecha de devolución'.");