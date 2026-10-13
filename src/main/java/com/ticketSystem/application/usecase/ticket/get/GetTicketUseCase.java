package com.ticketSystem.application.usecase.ticket.get;

import java.util.List;

import com.ticketSystem.application.port.data.ticket.CommentRepository;
import com.ticketSystem.application.port.data.ticket.TicketRepository;
import com.ticketSystem.application.usecase.core.UseCase;
import com.ticketSystem.application.usecase.ticket.access.TicketAccessStrategy;
import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.shared.exception.ResourceNotFound;

public final class GetTicketUseCase implements UseCase<GetTicketInput, Ticket>
{
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;

    public GetTicketUseCase(TicketRepository ticketRepository, CommentRepository commentRepository)
    {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Ticket execute(GetTicketInput input)
    {
        Ticket ticket = this.findTicket(input.ticketId(), input.authenticatedUser());
        ticket.addComments(this.findComments(ticket.getId()));

        return ticket;
    }

    private Ticket findTicket(String ticketId, User authenticatedUser)
    {
        return this.ticketRepository.findById(ticketId)
            .filter(TicketAccessStrategy.getStrategy(authenticatedUser))
            .orElseThrow(() -> new ResourceNotFound("Ticket", ticketId));
    }

    private List<Comment> findComments(String ticketId)
    {
        return this.commentRepository.findAllByTicketId(ticketId);
    }
}
