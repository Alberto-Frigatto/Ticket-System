package com.ticketSystem.application.usecase.ticket.create;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.ticketSystem.application.port.data.ticket.TicketRepository;
import com.ticketSystem.application.port.id.IdGenerator;
import com.ticketSystem.application.usecase.core.UseCase;
import com.ticketSystem.domain.ticket.Status;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.ticket.TicketBuilder;

public final class CreateTicketUseCase implements UseCase<CreateTicketInput, Ticket>
{
    private final IdGenerator idGenerator;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    public CreateTicketUseCase(
        IdGenerator idGenerator,
        TicketRepository ticketRepository,
        Clock clock
    ) {
        this.idGenerator = idGenerator;
        this.ticketRepository = ticketRepository;
        this.clock = clock;
    }

    @Override
    public Ticket execute(CreateTicketInput input)
    {
        Ticket newTicket = TicketBuilder.of()
            .id(this.idGenerator.generateId())
            .title(input.title())
            .description(input.description())
            .priority(input.priority())
            .category(input.category())
            .requester(input.authenticatedRequester())
            .createdAt(LocalDateTime.now(this.clock))
            .status(Status.OPEN)
            .comments(List.of())
            .build();

        this.ticketRepository.save(newTicket);

        return newTicket;
    }
}
