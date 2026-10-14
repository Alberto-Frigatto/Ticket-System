package com.ticketSystem.infra.data.ticket;

import java.util.List;
import java.util.Optional;

import com.ticketSystem.application.port.data.ticket.TicketRepository;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.infra.data.ticket.jpa.entity.CommentJpa;
import com.ticketSystem.infra.data.ticket.jpa.entity.TicketJpa;
import com.ticketSystem.infra.data.ticket.jpa.mapper.TicketJpaMapper;
import com.ticketSystem.infra.data.ticket.jpa.repository.CommentJpaRepository;
import com.ticketSystem.infra.data.ticket.jpa.repository.TicketJpaRepository;

public final class DefaultTicketRepository implements TicketRepository
{
    private final TicketJpaRepository ticketJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final TicketJpaMapper jpaMapper;

    public DefaultTicketRepository(
        TicketJpaRepository ticketJpaRepository,
        CommentJpaRepository commentJpaRepository,
        TicketJpaMapper jpaMapper
    ) {
        this.ticketJpaRepository = ticketJpaRepository;
        this.commentJpaRepository = commentJpaRepository;
        this.jpaMapper = jpaMapper;
    }

    @Override
    public void save(Ticket ticket)
    {
        this.ticketJpaRepository.save(this.jpaMapper.toJpa(ticket));
    }

    @Override
    public Optional<Ticket> findById(String id)
    {
        byte[] jpaId = this.jpaMapper.toJpaId(id);

        Optional<TicketJpa> ticket = this.ticketJpaRepository.findById(jpaId);

        if (ticket.isEmpty())
            return Optional.empty();

        List<CommentJpa> comments = this.commentJpaRepository.findAllByTicketId(jpaId);

        return Optional.of(this.jpaMapper.toEntity(ticket.get(), comments));
    }
}
