# create databases
CREATE DATABASE IF NOT EXISTS `spring_batch`;
CREATE DATABASE IF NOT EXISTS `app`;

# use database app
USE app;

# create client table
CREATE TABLE client(
    name TEXT,
    lastName TEXT,
    age TEXT,
    email TEXT
);

# insert clients
INSERT INTO client VALUES("Joao", "Silva", "30", "joao@test.com");
INSERT INTO client VALUES("Maria", "Silva", "30", "maria@test.com");