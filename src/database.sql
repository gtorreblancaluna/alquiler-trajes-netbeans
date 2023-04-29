CREATE TABLE catalog_status_event (
  id INT(10) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE catalog_type_event (
  id INT(10) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE users (
  id INT(10) NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NULL,
  password VARCHAR(255) NULL,
  name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  phone_number VARCHAR(255) NULL,
  updated_at TIMESTAMP NULL,
  created_at TIMESTAMP NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE customers (
  id INT(10) NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NULL,
  name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  phone_number1 VARCHAR(255) NOT NULL,
  phone_number2 VARCHAR(255) NULL,
  phone_number3 VARCHAR(255) NULL,
  updated_at TIMESTAMP NULL,
  created_at TIMESTAMP NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE events (
  id INT(10) NOT NULL AUTO_INCREMENT,
  user_id INT(10) DEFAULT NULL,
  catalog_type_event_id INT(10) DEFAULT NULL,
  catalog_status_event_id INT(10) DEFAULT NULL,
  customer_id INT(10) DEFAULT NULL,
  delivery_date TIMESTAMP NOT NULL,
  delivery_hour VARCHAR(55) NOT NULL,
  return_date TIMESTAMP NULL,
  return_hour VARCHAR(55) NOT NULL,
  updated_at TIMESTAMP NULL,
  created_at TIMESTAMP NULL,
  description VARCHAR(755) NOT NULL,
  comments VARCHAR(755) NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_events_user_id
    FOREIGN KEY (user_id) 
    REFERENCES users (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_events_customer_id
    FOREIGN KEY (customer_id) 
    REFERENCES customers (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_events_catalog_type_event_id
    FOREIGN KEY (catalog_type_event_id) 
    REFERENCES catalog_type_event (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_events_catalog_status_event_id
    FOREIGN KEY (catalog_status_event_id) 
    REFERENCES catalog_status_event (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE detail_event (
  id INT(10) NOT NULL AUTO_INCREMENT,
  event_id INT(10) DEFAULT NULL,
  -- agregado
  name_of_aggregate VARCHAR(355) NOT NULL,
  items VARCHAR(755) NULL,
  -- ajustes
  adjustments VARCHAR(755) NULL,
  unit_price FLOAT DEFAULT NULL,
  -- anticipo
  advance_payment FLOAT DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_detail_event_event_id
    FOREIGN KEY (event_id) 
    REFERENCES events (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE payments (
  id INT(10) NOT NULL AUTO_INCREMENT,
  user_id INT(10) DEFAULT NULL,
  event_id INT(10) DEFAULT NULL,
  comment VARCHAR(255) NULL,
  payment FLOAT DEFAULT NULL,
  updated_at TIMESTAMP NULL,
  created_at TIMESTAMP NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_payments_user_id
    FOREIGN KEY (user_id) 
    REFERENCES users (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_payments_event_id
    FOREIGN KEY (event_id) 
    REFERENCES events (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE roles (
  id INT(10) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE users_roles (
  user_id INT(10) DEFAULT NULL,
  role_id INT(10) DEFAULT NULL,
  CONSTRAINT fk_roles_user_roles 
    FOREIGN KEY (role_id) 
    REFERENCES roles (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_users_user_roles  
    FOREIGN KEY (user_id) 
    REFERENCES users (id) 
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
