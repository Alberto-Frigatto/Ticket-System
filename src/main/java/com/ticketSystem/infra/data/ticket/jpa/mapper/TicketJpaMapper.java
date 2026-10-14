package com.ticketSystem.infra.data.ticket.jpa.mapper;

import java.util.List;

import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.ticket.TicketBuilder;
import com.ticketSystem.infra.data.core.JpaMapper;
import com.ticketSystem.infra.data.ticket.jpa.entity.CommentJpa;
import com.ticketSystem.infra.data.ticket.jpa.entity.TicketJpa;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.id.UlidManager;

public final class TicketJpaMapper extends JpaMapper
{
    private final UserJpaMapper userJpaMapper;
    private final CommentJpaMapper commentJpaMapper;

    public TicketJpaMapper(
        UlidManager ulidManager,
        UserJpaMapper userJpaMapper,
        CommentJpaMapper commentJpaMapper
    ) {
        super(ulidManager);
        this.userJpaMapper = userJpaMapper;
        this.commentJpaMapper = commentJpaMapper;
    }

    public TicketJpa toJpa(Ticket ticket)
    {
        return new TicketJpa(
            super.toJpaId(ticket.getId()),
            this.userJpaMapper.toJpa(ticket.getRequester()),
            ticket.getCategory(),
            ticket.getCreatedAt(),
            ticket.getTitle(),
            ticket.getDescription(),
            ticket.getStatus(),
            ticket.getPriority(),
            ticket.getAssignee() != null ? this.userJpaMapper.toJpa(ticket.getAssignee()) : null,
            ticket.getClosedAt()
        );
    }

    public Ticket toEntity(TicketJpa jpa, List<CommentJpa> jpaComments)
    {
        List<Comment> comments = jpaComments.stream().map(this.commentJpaMapper::toEntity).toList();

        return TicketBuilder.of()
            .id(this.toEntityId(jpa.getId()))
            .requester(this.userJpaMapper.toEntity(jpa.getRequester()))
            .category(jpa.getCategory())
            .createdAt(jpa.getCreatedAt())
            .title(jpa.getTitle())
            .description(jpa.getDescription())
            .status(jpa.getStatus())
            .priority(jpa.getPriority())
            .assignee(jpa.getAssignee() != null ? this.userJpaMapper.toEntity(jpa.getAssignee()) : null)
            .closedAt(jpa.getClosedAt())
            .comments(comments)
            .build();
    }
}
