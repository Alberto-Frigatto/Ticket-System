CREATE TABLE comments (
    id BINARY(16) NOT NULL,
    ticket_id BINARY(16) NOT NULL,
    author_id BINARY(16) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    created_at DATETIME NOT NULL,

    CONSTRAINT pk_comments
        PRIMARY KEY (id),

    CONSTRAINT fk_comments_ticket
        FOREIGN KEY (ticket_id)
        REFERENCES tickets (id),

    CONSTRAINT fk_comments_author
        FOREIGN KEY (author_id)
        REFERENCES users (id)
);
