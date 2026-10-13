package com.ticketSystem.domain.ticket;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ticketSystem.domain.user.User;

public final class Ticket
{
    private final String id;
    private final User requester;
    private final Category category;
    private final LocalDateTime createdAt;
    private final List<Comment> comments;

    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private User assignee;
    private LocalDateTime closedAt;

    protected Ticket(
        String id,
        String title,
        String description,
        Status status,
        Priority priority,
        User assignee,
        LocalDateTime closedAt,
        User requester,
        Category category,
        LocalDateTime createdAt,
        List<Comment> comments
    ) {
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNull(description);
        this.status = Objects.requireNonNull(status);
        this.priority = Objects.requireNonNull(priority);
        this.assignee = assignee;
        this.closedAt = closedAt;
        this.requester = Objects.requireNonNull(requester);
        this.category = Objects.requireNonNull(category);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.comments = new ArrayList<>(Objects.requireNonNull(comments));

        this.checkInvariants();
    }

    private void checkInvariants()
    {
        if (status == Status.CLOSED && closedAt == null)
            throw new IllegalStateException("Closed ticket must have a closedAt timestamp.");

        if (status != Status.CLOSED && closedAt != null)
            throw new IllegalStateException("Open ticket cannot have a closedAt timestamp.");

        if (status == Status.CLOSED && assignee == null)
            throw new IllegalStateException("Closed ticket must have an assignee.");

        if (closedAt != null && closedAt.isBefore(createdAt))
            throw new IllegalStateException("ClosedAt timestamp cannot be before CreatedAt timestamp.");
    }

    public void assignTo(User assignee)
    {
        if (isClosed())
            throw new IllegalStateException("Cannot assign a closed ticket.");

        this.assignee = Objects.requireNonNull(assignee);
    }

    public void unassign()
    {
        if (isClosed())
            throw new IllegalStateException("Cannot unassign a closed ticket.");

        if (assignee == null)
            throw new IllegalStateException("Ticket is not assigned to anyone.");

        this.assignee = null;
    }

    public void close(Clock clock)
    {
        if (isClosed())
            throw new IllegalStateException("Ticket is already closed.");

        if (assignee == null)
            throw new IllegalStateException("Cannot close a ticket without an assignee.");

        LocalDateTime now = LocalDateTime.now(clock);

        if (now.isBefore(this.createdAt))
            throw new IllegalStateException("ClosedAt timestamp cannot be before CreatedAt timestamp.");

        this.status = Status.CLOSED;
        this.closedAt = now;
    }

    public boolean isClosed()
    {
        return status == Status.CLOSED;
    }

    public LocalDateTime getDeadline()
    {
        return createdAt.plus(priority.getDuration());
    }

    public void addComment(Comment comment)
    {
        if (isClosed())
            throw new IllegalStateException("Ticket is already closed.");

        this.comments.add(Objects.requireNonNull(comment));
    }

    public void addComments(List<Comment> comments)
    {
        if (isClosed())
            throw new IllegalStateException("Ticket is already closed.");

        this.comments.addAll(Objects.requireNonNull(comments));
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public Status getStatus()
    {
        return status;
    }

    public Priority getPriority()
    {
        return priority;
    }

    public User getAssignee()
    {
        return assignee;
    }

    public LocalDateTime getClosedAt()
    {
        return closedAt;
    }

    public User getRequester()
    {
        return requester;
    }

    public Category getCategory()
    {
        return category;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public List<Comment> getComments()
    {
        return List.copyOf(comments);
    }
}
