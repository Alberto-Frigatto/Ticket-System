package com.ticketSystem.infra.data.ticket.jpa.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.ticketSystem.infra.data.ticket.jpa.entity.TicketJpa;

public interface TicketJpaRepository extends Repository<TicketJpa, byte[]>
{
    void save(TicketJpa ticket);
    Optional<TicketJpa> findById(byte[] id);
}
