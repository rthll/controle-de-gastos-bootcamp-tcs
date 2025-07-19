CREATE DATABASE users_db;
CREATE DATABASE gastos_db;
CREATE DATABASE category_db;
CREATE DATABASE investimentos_db;
CREATE DATABASE recovery_db;
CREATE DATABASE divisao_db;
CREATE DATABASE employee_db;
CREATE DATABASE sector_db;

\c users_db;

DO $$ BEGIN
CREATE TYPE user_role AS ENUM ('PESSOAL', 'EMPRESARIAL');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE OR REPLACE FUNCTION sp_find_user_by_email(p_email VARCHAR)
RETURNS TABLE(
    id BIGINT,
    name VARCHAR,
    email VARCHAR,
    password VARCHAR,
    role VARCHAR
) AS $$
BEGIN
RETURN QUERY
SELECT u.id, u.name, u.email, u.password, u.role::VARCHAR
FROM users u
WHERE u.email = p_email;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_register_user(
    p_name VARCHAR,
    p_email VARCHAR,
    p_password VARCHAR,
    p_role VARCHAR DEFAULT 'PESSOAL'
) RETURNS BIGINT AS $$
DECLARE
user_id BIGINT;
BEGIN
INSERT INTO users (name, email, password, role)
VALUES (p_name, p_email, p_password, p_role::user_role)
    RETURNING id INTO user_id;

RETURN user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_update_user(
    p_email VARCHAR,
    p_name VARCHAR DEFAULT NULL,
    p_password VARCHAR DEFAULT NULL
) RETURNS VOID AS $$
BEGIN
UPDATE users
SET
    name = COALESCE(p_name, name),
    password = COALESCE(p_password, password)
WHERE email = p_email;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_switch_user_role(
    p_email VARCHAR,
    p_new_role VARCHAR
) RETURNS VOID AS $$
BEGIN
UPDATE users
SET role = p_new_role::user_role
WHERE email = p_email;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_user_exists(p_email VARCHAR)
RETURNS BOOLEAN AS $$
DECLARE
user_count INTEGER;
BEGIN
SELECT COUNT(*) INTO user_count
FROM users
WHERE email = p_email;

RETURN user_count > 0;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_update_user_password(
    p_email VARCHAR,
    p_new_password VARCHAR
) RETURNS VOID AS $$
BEGIN
UPDATE users
SET password = p_new_password
WHERE email = p_email;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_get_user_role(p_email VARCHAR)
RETURNS VARCHAR AS $$
DECLARE
user_role VARCHAR;
BEGIN
SELECT role::VARCHAR INTO user_role
FROM users
WHERE email = p_email;

RETURN user_role;
END;
$$ LANGUAGE plpgsql;

DO $$ BEGIN
CREATE TYPE employee_role AS ENUM ('PESSOAL', 'EMPRESARIAL');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE OR REPLACE FUNCTION sp_find_user_by_email(p_email VARCHAR)
RETURNS TABLE(
    id BIGINT,
    name VARCHAR,
    email VARCHAR,
    password VARCHAR,
    role VARCHAR
) AS $$
BEGIN
RETURN QUERY
SELECT u.id, u.name, u.email, u.password, u.role::VARCHAR
FROM users u
WHERE u.email = p_email;
END;
$$ LANGUAGE plpgsql;