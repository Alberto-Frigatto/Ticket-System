package com.ticketSystem.application.port.data.ticket;

import java.util.Optional;

import com.ticketSystem.domain.ticket.Ticket;

public interface TicketRepository
{
    void save(Ticket ticket);
    Optional<Ticket> findById(String id);
}
