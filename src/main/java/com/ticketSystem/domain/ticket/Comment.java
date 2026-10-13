package com.ticketSystem.domain.ticket;

import java.time.LocalDateTime;
import java.util.Objects;

import com.ticketSystem.domain.user.User;

public final class Comment
{
    private final String id;
    private final User author;
    private final String content;
    private final LocalDateTime createdAt;

    public Comment(
        String id,
        User author,
        String content,
        LocalDateTime createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.author = Objects.requireNonNull(author);
        this.content = Objects.requireNonNull(content);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public String getId()
    {
        return id;
    }

    public User getAuthor()
    {
        return author;
    }

    public String getContent()
    {
        return content;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
}
