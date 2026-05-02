ALTER TABLE users
ADD COLUMN must_change_password BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE users
ADD COLUMN onboarding_completed BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE users
SET must_change_password = FALSE,
    onboarding_completed = TRUE
WHERE role = 'ADMIN';