package com.ticketSystem.infra.data.ticket;

import java.util.List;

import com.ticketSystem.application.port.data.ticket.CommentRepository;
import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.infra.data.ticket.jpa.mapper.CommentJpaMapper;
import com.ticketSystem.infra.data.ticket.jpa.repository.CommentJpaRepository;

public final class DefaultCommentRepository implements CommentRepository
{
    private final CommentJpaRepository jpaRepository;
    private final CommentJpaMapper mapper;

    public DefaultCommentRepository(CommentJpaRepository jpaRepository, CommentJpaMapper mapper)
    {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(String ticketId, Comment comment)
    {
        this.jpaRepository.save(this.mapper.toJpa(comment, ticketId));
    }

    @Override
    public List<Comment> findAllByTicketId(String ticketId)
    {
        return this.jpaRepository.findAllByTicketId(this.mapper.toJpaId(ticketId))
            .stream()
            .map(this.mapper::toEntity)
            .toList();
    }
}
