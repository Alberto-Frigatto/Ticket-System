package com.ticketSystem.domain.ticket;

import java.time.LocalDateTime;
import java.util.List;

import com.ticketSystem.domain.user.User;

public class TicketBuilder
{
    private String id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private User assignee;
    private LocalDateTime closedAt;
    private User requester;
    private Category category;
    private LocalDateTime createdAt;
    private List<Comment> comments;

    private TicketBuilder() {}

    public static TicketBuilder of()
    {
        return new TicketBuilder();
    }

    public TicketBuilder id(String id)
    {
        this.id = id;
        return this;
    }

    public TicketBuilder title(String title)
    {
        this.title = title;
        return this;
    }

    public TicketBuilder description(String description)
    {
        this.description = description;
        return this;
    }

    public TicketBuilder status(Status status)
    {
        this.status = status;
        return this;
    }

    public TicketBuilder priority(Priority priority)
    {
        this.priority = priority;
        return this;
    }

    public TicketBuilder assignee(User assignee)
    {
        this.assignee = assignee;
        return this;
    }

    public TicketBuilder closedAt(LocalDateTime closedAt)
    {
        this.closedAt = closedAt;
        return this;
    }

    public TicketBuilder requester(User requester)
    {
        this.requester = requester;
        return this;
    }

    public TicketBuilder category(Category category)
    {
        this.category = category;
        return this;
    }

    public TicketBuilder createdAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

    public TicketBuilder comments(List<Comment> comments)
    {
        this.comments = comments;
        return this;
    }

    public Ticket build()
    {
        return new Ticket(
            id,
            title,
            description,
            status,
            priority,
            assignee,
            closedAt,
            requester,
            category,
            createdAt,
            comments
        );
    }
}
