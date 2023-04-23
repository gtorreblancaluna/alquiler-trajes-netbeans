-- passwd = 123456
INSERT INTO users (email,password,name,last_name,phone_number,enabled,created_at,updated_at) 
    VALUES ('jerry@gmail.com','6fd702893abe7973','Gerardo','Torreblanca','55555','1','2023-01-01','2023-01-01');

INSERT INTO roles (name) VALUES ('Admin');
INSERT INTO roles (name) VALUES ('Default');

INSERT INTO users_roles (user_id,role_id) VALUES (1,1);