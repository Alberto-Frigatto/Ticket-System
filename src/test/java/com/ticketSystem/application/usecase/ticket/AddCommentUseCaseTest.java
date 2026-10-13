package com.ticketSystem.application.usecase.ticket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ticketSystem.application.port.data.ticket.CommentRepository;
import com.ticketSystem.application.port.data.ticket.TicketRepository;
import com.ticketSystem.application.port.id.IdGenerator;
import com.ticketSystem.application.usecase.ticket.comment.add.AddCommentInput;
import com.ticketSystem.application.usecase.ticket.comment.add.AddCommentUseCase;
import com.ticketSystem.domain.ticket.Category;
import com.ticketSystem.domain.ticket.Priority;
import com.ticketSystem.domain.ticket.Status;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.ticket.TicketBuilder;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;
import com.ticketSystem.shared.exception.ResourceNotFound;

class AddCommentUseCaseTest
{
    @Mock private TicketRepository ticketRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private IdGenerator idGenerator;

    private Clock clock;
    private AddCommentUseCase useCase;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

        clock = Clock.fixed(
            Instant.parse("2026-09-17T21:00:00Z"),
            ZoneOffset.UTC
        );

        useCase = new AddCommentUseCase(
            ticketRepository,
            commentRepository,
            idGenerator,
            clock
        );
    }

    User buildValidRequester()
    {
        return UserBuilder.of()
            .id("user-123")
            .name("John Doe")
            .email("john.doe@example.com")
            .role(Role.REQUESTER)
            .department(Department.IT)
            .build();
    }

    User buildValidRequester(String id)
    {
        return UserBuilder.of()
            .id(id)
            .name("John Doe")
            .email("john.doe@example.com")
            .role(Role.REQUESTER)
            .department(Department.IT)
            .build();
    }

    Ticket buildTicket(User requester)
    {
        return TicketBuilder.of()
            .id("ticket-123")
            .title("Computer does not start")
            .description("The computer does not turn on.")
            .priority(Priority.HIGH)
            .category(Category.HARDWARE)
            .requester(requester)
            .status(Status.OPEN)
            .createdAt(LocalDateTime.now())
            .comments(List.of())
            .build();
    }

    @Test
    void should_AddComment_When_UserHasAccessToTicket()
    {
        String commentId = "comment-id";
        String content = "Problema resolvido.";

        User authenticatedUser = buildValidRequester();

        Ticket ticket = buildTicket(authenticatedUser);

        AddCommentInput input = new AddCommentInput(
            ticket.getId(),
            content,
            authenticatedUser
        );

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(idGenerator.generateId()).thenReturn(commentId);

        Ticket result = useCase.execute(input);

        assertThat(result).isSameAs(ticket);

        verify(idGenerator).generateId();
    }

    @Test
    void should_Throw_When_TicketDoesNotExist()
    {
        String ticketId = "ticket-id";

        AddCommentInput input = new AddCommentInput(
            ticketId,
            "Problema resolvido.",
            buildValidRequester()
        );

        when(ticketRepository.findById(ticketId))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(input))
            .isInstanceOf(ResourceNotFound.class);

        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void should_Throw_When_UserDoesNotHaveAccessToTicket()
    {
        String ticketId = "ticket-id";

        AddCommentInput input = new AddCommentInput(
            ticketId,
            "Problema resolvido.",
            buildValidRequester("test-1")
        );

        when(ticketRepository.findById(ticketId))
            .thenReturn(Optional.of(buildTicket(buildValidRequester("test-2"))));
        when(idGenerator.generateId()).thenReturn("123");

        assertThatThrownBy(() -> useCase.execute(input))
            .isInstanceOf(ResourceNotFound.class);

        verify(ticketRepository).findById(ticketId);
    }
}
