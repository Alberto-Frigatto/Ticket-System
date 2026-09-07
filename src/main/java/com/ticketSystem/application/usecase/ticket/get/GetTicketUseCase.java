package com.ticketSystem.application.usecase.ticket.get;

import com.ticketSystem.application.port.data.ticket.TicketRepository;
import com.ticketSystem.application.usecase.core.UseCase;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.shared.exception.ResourceNotFound;

public final class GetTicketUseCase implements UseCase<GetTicketInput, Ticket>
{
    private final TicketRepository ticketRepository;

    public GetTicketUseCase(TicketRepository ticketRepository)
    {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket execute(GetTicketInput input)
    {
        return this.ticketRepository.findById(input.ticketId())
            .filter(GetTicketAccessStrategy.getStrategy(input.authenticatedUser()))
            .orElseThrow(() -> new ResourceNotFound("Ticket", input.ticketId()));
    }
}
