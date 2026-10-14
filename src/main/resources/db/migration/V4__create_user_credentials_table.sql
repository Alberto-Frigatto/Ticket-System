CREATE TABLE user_credentials (
    user_id BINARY(16) NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_credentials PRIMARY KEY (user_id)
);
