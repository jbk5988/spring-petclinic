CREATE DATABASE petclinic;

ALTER DATABASE petclinic
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON petclinic.* TO pc@localhost IDENTIFIED BY 'pc';

USE petclinic;

CREATE SEQUENCE vets_seq;

CREATE TABLE IF NOT EXISTS vets (
  id INT CHECK (id > 0) NOT NULL DEFAULT NEXTVAL ('vets_seq') PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30)
  CREATE INDEX(last_name)
); engine=InnoDB;

CREATE SEQUENCE specialties_seq;

CREATE TABLE IF NOT EXISTS specialties (
  id INT CHECK (id > 0) NOT NULL DEFAULT NEXTVAL ('specialties_seq') PRIMARY KEY,
  name VARCHAR(80)
  CREATE INDEX(name)
); engine=InnoDB;

CREATE TABLE IF NOT EXISTS vet_specialties (
  vet_id INT CHECK (vet_id > 0) NOT NULL,
  specialty_id INT CHECK (specialty_id > 0) NOT NULL,
  FOREIGN KEY (vet_id) REFERENCES vets(id),
  FOREIGN KEY (specialty_id) REFERENCES specialties(id),
  UNIQUE (vet_id,specialty_id)
); engine=InnoDB;

CREATE SEQUENCE types_seq;

CREATE TABLE IF NOT EXISTS types (
  id INT CHECK (id > 0) NOT NULL DEFAULT NEXTVAL ('types_seq') PRIMARY KEY,
  name VARCHAR(80)
  CREATE INDEX(name)
); engine=InnoDB;

CREATE SEQUENCE owners_seq;

CREATE TABLE IF NOT EXISTS owners (
  id INT CHECK (id > 0) NOT NULL DEFAULT NEXTVAL ('owners_seq') PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20)
  CREATE INDEX(last_name)
); engine=InnoDB;

CREATE SEQUENCE pets_seq;

CREATE TABLE IF NOT EXISTS pets (
  id INT CHECK (id > 0) NOT NULL DEFAULT NEXTVAL ('pets_seq') PRIMARY KEY,
  name VARCHAR(30),
  birth_date DATE,
  type_id INT CHECK (type_id > 0) NOT NULL,
  owner_id INT CHECK (owner_id > 0) NOT NULL
  CREATE INDEX(name),
  FOREIGN KEY (owner_id) REFERENCES owners(id),
  FOREIGN KEY (type_id) REFERENCES types(id)
); engine=InnoDB;

CREATE SEQUENCE visits_seq;

CREATE TABLE IF NOT EXISTS visits (
  id INT CHECK (id > 0) NOT NULL DEFAULT NEXTVAL ('visits_seq') PRIMARY KEY,
  pet_id INT CHECK (pet_id > 0) NOT NULL,
  visit_date DATE,
  description VARCHAR(255),
  FOREIGN KEY (pet_id) REFERENCES pets(id)
); engine=InnoDB;
