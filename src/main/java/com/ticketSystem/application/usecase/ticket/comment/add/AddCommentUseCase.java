package com.ticketSystem.application.usecase.ticket.comment.add;

import java.time.Clock;
import java.time.LocalDateTime;

import com.ticketSystem.application.port.data.ticket.CommentRepository;
import com.ticketSystem.application.port.data.ticket.TicketRepository;
import com.ticketSystem.application.port.id.IdGenerator;
import com.ticketSystem.application.usecase.core.UseCase;
import com.ticketSystem.application.usecase.ticket.access.TicketAccessStrategy;
import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.shared.exception.ResourceNotFound;

public final class AddCommentUseCase implements UseCase<AddCommentInput, Ticket>
{
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final IdGenerator idGenerator;
    private final Clock clock;

    public AddCommentUseCase(
        TicketRepository ticketRepository,
        CommentRepository commentRepository,
        IdGenerator idGenerator,
        Clock clock
    ) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.idGenerator = idGenerator;
        this.clock = clock;
    }

    @Override
    public Ticket execute(AddCommentInput input)
    {
        Ticket ticket = this.findTicket(input.ticketId(), input.authenticatedUser());

        Comment comment = new Comment(
            this.idGenerator.generateId(),
            input.authenticatedUser(),
            input.content(),
            LocalDateTime.now(this.clock)
        );

        this.commentRepository.save(ticket.getId(), comment);

        ticket.addComment(comment);

        return ticket;
    }

    private Ticket findTicket(String ticketId, User authenticatedUser)
    {
        return this.ticketRepository.findById(ticketId)
            .filter(TicketAccessStrategy.getStrategy(authenticatedUser))
            .orElseThrow(() -> new ResourceNotFound("Ticket", ticketId));
    }
}
