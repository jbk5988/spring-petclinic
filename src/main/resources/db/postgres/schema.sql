CREATE DATABASE IF NOT EXISTS petclinic;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

CREATE SEQUENCE owners_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE owners_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

CREATE TABLE owners (
    id integer DEFAULT nextval('owners_seq'::regclass) NOT NULL,
    first_name character varying(30),
    last_name character varying(30),
    address character varying(255),
    city character varying(80),
    telephone character varying(20),
    CONSTRAINT owners_id_check CHECK ((id > 0))
);

CREATE SEQUENCE pets_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE pets_seq OWNER TO postgres;

CREATE TABLE pets (
    id integer DEFAULT nextval('pets_seq'::regclass) NOT NULL,
    name character varying(30),
    birth_date date,
    type_id integer NOT NULL,
    owner_id integer NOT NULL,
    CONSTRAINT pets_id_check CHECK ((id > 0)),
    CONSTRAINT pets_owner_id_check CHECK ((owner_id > 0)),
    CONSTRAINT pets_type_id_check CHECK ((type_id > 0))
);

ALTER TABLE pets OWNER TO postgres;

CREATE SEQUENCE specialties_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE specialties_seq OWNER TO postgres;

CREATE TABLE specialties (
    id integer DEFAULT nextval('specialties_seq'::regclass) NOT NULL,
    name character varying(80),
    CONSTRAINT specialties_id_check CHECK ((id > 0))
);

ALTER TABLE specialties OWNER TO postgres;

CREATE SEQUENCE types_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE types_seq OWNER TO postgres;

CREATE TABLE types (
    id integer DEFAULT nextval('types_seq'::regclass) NOT NULL,
    name character varying(80),
    CONSTRAINT types_id_check CHECK ((id > 0))
);

ALTER TABLE types OWNER TO postgres;

CREATE TABLE vet_specialties (
    vet_id integer NOT NULL,
    specialty_id integer NOT NULL,
    CONSTRAINT vet_specialties_specialty_id_check CHECK ((specialty_id > 0)),
    CONSTRAINT vet_specialties_vet_id_check CHECK ((vet_id > 0))
);

ALTER TABLE vet_specialties OWNER TO postgres;

CREATE SEQUENCE vets_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vets_seq OWNER TO postgres;

CREATE TABLE vets (
    id integer DEFAULT nextval('vets_seq'::regclass) NOT NULL,
    first_name character varying(30),
    last_name character varying(30),
    CONSTRAINT vets_id_check CHECK ((id > 0))
);


ALTER TABLE vets OWNER TO postgres;

CREATE SEQUENCE visits_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE visits_seq OWNER TO postgres;

CREATE TABLE visits (
    id integer DEFAULT nextval('visits_seq'::regclass) NOT NULL,
    pet_id integer NOT NULL,
    visit_date date,
    description character varying(255),
    CONSTRAINT visits_id_check CHECK ((id > 0)),
    CONSTRAINT visits_pet_id_check CHECK ((pet_id > 0))
);

ALTER TABLE visits OWNER TO postgres;

ALTER TABLE ONLY owners
    ADD CONSTRAINT owners_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pets
    ADD CONSTRAINT pets_pkey PRIMARY KEY (id);

ALTER TABLE ONLY specialties
    ADD CONSTRAINT specialties_pkey PRIMARY KEY (id);

ALTER TABLE ONLY types
    ADD CONSTRAINT types_pkey PRIMARY KEY (id);

ALTER TABLE ONLY vet_specialties
    ADD CONSTRAINT vet_specialties_vet_id_specialty_id_key UNIQUE (vet_id, specialty_id);

ALTER TABLE ONLY vets
    ADD CONSTRAINT vets_pkey PRIMARY KEY (id);

ALTER TABLE ONLY visits
    ADD CONSTRAINT visits_pkey PRIMARY KEY (id);

CREATE INDEX owners_last_name_idx ON owners USING btree (last_name);

CREATE INDEX pets_name_idx ON pets USING btree (name);

CREATE INDEX specialties_name_idx ON specialties USING btree (name);

CREATE INDEX types_name_idx ON types USING btree (name);

CREATE INDEX vets_last_name_idx ON vets USING btree (last_name);

ALTER TABLE ONLY pets
    ADD CONSTRAINT pets_owner_id_fkey FOREIGN KEY (owner_id) REFERENCES owners(id);

ALTER TABLE ONLY pets
    ADD CONSTRAINT pets_type_id_fkey FOREIGN KEY (type_id) REFERENCES types(id);

ALTER TABLE ONLY vet_specialties
    ADD CONSTRAINT vet_specialties_specialty_id_fkey FOREIGN KEY (specialty_id) REFERENCES specialties(id);

ALTER TABLE ONLY vet_specialties
    ADD CONSTRAINT vet_specialties_vet_id_fkey FOREIGN KEY (vet_id) REFERENCES vets(id);

ALTER TABLE ONLY visits
    ADD CONSTRAINT visits_pet_id_fkey FOREIGN KEY (pet_id) REFERENCES pets(id);
