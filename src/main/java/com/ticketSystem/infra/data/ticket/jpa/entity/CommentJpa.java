package com.ticketSystem.infra.data.ticket.jpa.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.ticketSystem.infra.data.user.jpa.entity.UserJpa;

@Entity
@Table(name = "comments")
public final class CommentJpa
{
    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", nullable = false, updatable = false, length = 16)
    private byte[] id;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "ticket_id", nullable = false, updatable = false, length = 16)
    private byte[] ticketId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "author_id",
        nullable = false,
        updatable = false,
        foreignKey = @ForeignKey(name = "fk_comments_author")
    )
    private UserJpa author;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected CommentJpa() {}

    public CommentJpa(
        byte[] id,
        byte[] ticketId,
        UserJpa author,
        String content,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.ticketId = ticketId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }

    public byte[] getId()
    {
        return id;
    }

    public byte[] getTicketId()
    {
        return ticketId;
    }

    public UserJpa getAuthor()
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
