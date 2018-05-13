SET ROLE TO flyway;
GRANT USAGE ON SCHEMA public TO app;

-- role 'app' needs privileges to do it's work
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO app;

-- simple table for the test
CREATE TABLE EMPLOYEE(
    ID TEXT NOT NULL,
    ACTIVE BOOLEAN NOT NULL
);