CREATE TABLE IF NOT EXISTS user (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL       UNIQUE,
    type        VARCHAR(16)  NOT NULL       CHECK type IN ('ADMIN', 'SELLER'),
    user_since  TIMESTAMP    NOT NULL,
    last_login  TIMESTAMP
);