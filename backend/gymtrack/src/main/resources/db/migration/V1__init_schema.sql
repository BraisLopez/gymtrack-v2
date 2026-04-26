-- Initial schema setup for GymTrack app

-- Base Table for Profiles (Clients and Trainers)
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL CHECK (role IN ('ADMIN', 'TRAINER', 'CLIENT')),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table for Memberships
CREATE TABLE memberships (
    membership_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    price DECIMAL(5, 2) NOT NULL,
    duration_days INT NOT NULL,
    level VARCHAR(30) NOT NULL CHECK (level IN ('BASIC', 'PREMIUM')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table for Clients
CREATE TABLE client_profiles (
    client_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    last_name_1 VARCHAR(255) NOT NULL,
    last_name_2 VARCHAR(255),
    birth_date DATE NOT NULL,
    goal VARCHAR(255),
    profile_picture_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table relationship Clients-Memberships
CREATE TABLE client_memberships(
    client_membership_id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES client_profiles(client_id) ON DELETE CASCADE,
    membership_id BIGINT NOT NULL REFERENCES memberships(membership_id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (end_date > start_date)
);

-- Table for Trainers
CREATE TABLE trainer_profiles (
    trainer_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    last_name_1 VARCHAR(255) NOT NULL,
    last_name_2 VARCHAR(255),
    specialty VARCHAR(63),
    bio VARCHAR(255),
    profile_picture_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- N:M relationship table for Clients-Trainers
CREATE TABLE follows(
    client_id BIGINT NOT NULL REFERENCES client_profiles(client_id) ON DELETE CASCADE,
    trainer_id BIGINT NOT NULL REFERENCES trainer_profiles(trainer_id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (client_id, trainer_id)
);

-- Table for Reviews from Clients to Trainers
-- (only 1 review per client-trainer pair)
CREATE TABLE reviews (
    review_id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES client_profiles(client_id) ON DELETE CASCADE,
    trainer_id BIGINT NOT NULL REFERENCES trainer_profiles(trainer_id) ON DELETE CASCADE,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (client_id, trainer_id)
);

-- Table exercises
CREATE TABLE exercises(
    exercise_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    video_url VARCHAR(500),
    muscle_group VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table Routines
CREATE TABLE routines(
    routine_id BIGSERIAL PRIMARY KEY,
    trainer_id BIGINT NOT NULL REFERENCES trainer_profiles(trainer_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    level VARCHAR(30) NOT NULL CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    estimated_duration_minutes INT NOT NULL CHECK (estimated_duration_minutes > 0),
    required_membership_level VARCHAR(30) NOT NULL CHECK (required_membership_level IN ('BASIC', 'PREMIUM')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (trainer_id, name)
);

-- Table relationship Routines-Exercises
CREATE TABLE routine_exercises(
    routine_exercise_id BIGSERIAL PRIMARY KEY,
    exercise_id BIGINT NOT NULL REFERENCES exercises(exercise_id) ON DELETE CASCADE,
    routine_id BIGINT NOT NULL REFERENCES routines(routine_id) ON DELETE CASCADE,
    sets INT NOT NULL CHECK (sets > 0),
    reps INT NOT NULL CHECK (reps > 0),
    rest_seconds INT NOT NULL CHECK (rest_seconds >= 0),
    order_index INT NOT NULL CHECK (order_index > 0),
    notes VARCHAR(255),
    UNIQUE (routine_id, order_index)
);

-- Table relationship Clients-Routines
-- for tracking which routines clients have accessed or completed
CREATE TABLE client_routines(
    client_routine_id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES client_profiles(client_id) ON DELETE CASCADE,
    routine_id BIGINT NOT NULL REFERENCES routines(routine_id) ON DELETE CASCADE,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (completed_at IS NULL OR started_at IS NOT NULL)
);