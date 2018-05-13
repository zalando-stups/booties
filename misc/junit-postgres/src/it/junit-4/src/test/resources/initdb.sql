-- create needed roles and databases, do not used postgres/postgres on db postgres

-- roles

-- role 'flyway' will be used to create/modify schemas during application
-- startup, flyway will also be used as the owner of the db (see below)
CREATE ROLE flyway WITH LOGIN CREATEDB CREATEROLE PASSWORD 'test';

-- role 'app' will be used for data manipulation in the application
CREATE ROLE app WITH LOGIN PASSWORD 'test';

-- databases
CREATE DATABASE junit5_test OWNER flyway;