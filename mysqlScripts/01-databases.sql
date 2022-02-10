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
INSERT INTO client VALUES("Zeca 1", "Silva", "30", "zeca1@test.com");
INSERT INTO client VALUES("Zeca 2", "Silva", "30", "zeca2@test.com");
INSERT INTO client VALUES("Zeca 3", "Silva", "30", "zeca3@test.com");
INSERT INTO client VALUES("Zeca 4", "Silva", "30", "zeca4@test.com");
INSERT INTO client VALUES("Zeca 5", "Silva", "30", "zeca5@test.com");
INSERT INTO client VALUES("Zeca 6", "Silva", "30", "zeca6@test.com");
INSERT INTO client VALUES("Zeca 7", "Silva", "30", "zeca7@test.com");
INSERT INTO client VALUES("Zeca 8", "Silva", "30", "zeca8@test.com");
INSERT INTO client VALUES("Zeca 9", "Silva", "30", "zeca9@test.com");