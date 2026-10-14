package com.ticketSystem.infra.data.ticket.jpa.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.ticketSystem.domain.ticket.Category;
import com.ticketSystem.domain.ticket.Priority;
import com.ticketSystem.domain.ticket.Status;
import com.ticketSystem.infra.data.user.jpa.entity.UserJpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public final class TicketJpa
{
    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", nullable = false, updatable = false, length = 16)
    private byte[] id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "requester_id",
        nullable = false,
        updatable = false,
        foreignKey = @ForeignKey(name = "fk_tickets_requester")
    )
    private UserJpa requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;


    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (
        name = "assignee_id",
        foreignKey = @ForeignKey(name = "fk_tickets_assignee")
    )
    private UserJpa assignee;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    protected TicketJpa() {}

    public TicketJpa(
        byte[] id,
        UserJpa requester,
        Category category,
        LocalDateTime createdAt,
        String title,
        String description,
        Status status,
        Priority priority,
        UserJpa assignee,
        LocalDateTime closedAt
    ) {
        this.id = id;
        this.requester = requester;
        this.category = category;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
        this.closedAt = closedAt;
    }

    public byte[] getId()
    {
        return id;
    }

    public UserJpa getRequester()
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

    public UserJpa getAssignee()
    {
        return assignee;
    }

    public LocalDateTime getClosedAt()
    {
        return closedAt;
    }
}
