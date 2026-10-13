CREATE TABLE tickets (
    id BINARY(16) NOT NULL,
    requester_id BINARY(16) NOT NULL,
    category VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    status VARCHAR(20) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    assignee_id BINARY(16),
    closed_at DATETIME,

    CONSTRAINT pk_tickets
        PRIMARY KEY (id),

    CONSTRAINT fk_tickets_requester
        FOREIGN KEY (requester_id)
        REFERENCES users (id),

    CONSTRAINT fk_tickets_assignee
        FOREIGN KEY (assignee_id)
        REFERENCES users (id)
);
