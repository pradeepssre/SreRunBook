-- Idempotent PostgreSQL Database Setup Script
-- Safe to run multiple times - only creates what doesn't exist
-- Run as superuser: psql -U postgres -f setup-database.sql

-- Configuration (modify these values as needed)
\set db_name 'studentdb'
\set db_user 'student_user'
\set db_password 'student_pass'

\echo 'Starting idempotent database setup...'
\echo 'Database: ' :db_name
\echo 'User: ' :db_user
\echo ''

-- Create user only if it doesn't exist
DO $$
BEGIN5432
    IF NOT EXISTS (SELECT 1 FROM pg_user WHERE usename = 'student_user') THEN
        CREATE USER student_user WITH 
            PASSWORD 'student_pass'
            CREATEDB
            LOGIN
            NOSUPERUSER
            NOCREATEROLE
            INHERIT
            NOREPLICATION
            CONNECTION LIMIT -1;
        RAISE NOTICE 'User student_user created successfully';
    ELSE
        RAISE NOTICE 'User student_user already exists, skipping creation';
        
        -- Update password in case it changed
        ALTER USER student_user WITH PASSWORD 'student_pass';
        RAISE NOTICE 'User password updated';
    END IF;
END $$;

-- Create database only if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'studentdb') THEN
        RAISE NOTICE 'Database studentdb does not exist';
        RAISE NOTICE 'Please run: CREATE DATABASE studentdb WITH OWNER = student_user ENCODING = ''UTF8'';';
    ELSE
        RAISE NOTICE 'Database studentdb already exists, skipping creation';
    END IF;
END $$;

-- Alternative approach: Use a more reliable conditional database creation
SELECT 'CREATE DATABASE ' || quote_ident('studentdb') || 
       ' WITH OWNER = ' || quote_ident('student_user') || 
       ' ENCODING = ''UTF8'' LC_COLLATE = ''en_US.UTF-8'' LC_CTYPE = ''en_US.UTF-8'' TEMPLATE = template0'
WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'studentdb') \gexec

-- Grant database privileges (safe even if database already exists)
GRANT ALL PRIVILEGES ON DATABASE studentdb TO student_user;
GRANT CONNECT ON DATABASE studentdb TO student_user;

-- Connect to the database
\c studentdb;

-- Create extensions if they don't exist
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Grant schema permissions (safe to run multiple times)
GRANT ALL ON SCHEMA public TO student_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO student_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO student_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO student_user;

-- Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO student_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO student_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO student_user;

-- Verification and summary
\echo ''
\echo '✅ Setup completed successfully!'
\echo ''

SELECT 
    'Setup Summary' as status,
    current_database() as database_name,
    current_user as connected_as,
    (SELECT usename FROM pg_user WHERE usename = 'student_user') as target_user_exists,
    (SELECT datname FROM pg_database WHERE datname = 'studentdb') as target_db_exists;

\echo ''
\echo 'Connection string: postgresql://student_user:student_pass@localhost:5432/studentdb'
\echo 'Test connection: psql -h localhost -U student_user -d studentdb'
\echo ''