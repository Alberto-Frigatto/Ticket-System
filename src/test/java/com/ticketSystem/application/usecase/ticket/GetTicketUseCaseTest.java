package com.ticketSystem.application.usecase.ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.ticketSystem.application.port.data.ticket.CommentRepository;
import com.ticketSystem.application.port.data.ticket.TicketRepository;
import com.ticketSystem.application.usecase.ticket.get.GetTicketInput;
import com.ticketSystem.application.usecase.ticket.get.GetTicketUseCase;
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

public class GetTicketUseCaseTest
{
    @Mock TicketRepository ticketRepository;
    @Mock CommentRepository commentRepository;

    GetTicketUseCase getTicketUseCase;

    @BeforeEach void setup()
    {
        MockitoAnnotations.openMocks(this);

        getTicketUseCase = new GetTicketUseCase(
            ticketRepository,
            commentRepository
        );
    }

    User buildUser(String id, Role role)
    {
        return UserBuilder.of()
            .id(id)
            .name("John Doe")
            .email("john.doe@example.com")
            .role(role)
            .department(Department.IT)
            .build();
    }

    Ticket buildTicket(User requester, User assignee)
    {
        return TicketBuilder.of()
            .id("ticket-123")
            .title("Computer does not start")
            .description("The computer does not turn on.")
            .priority(Priority.HIGH)
            .category(Category.HARDWARE)
            .requester(requester)
            .assignee(assignee)
            .status(Status.OPEN)
            .createdAt(LocalDateTime.now())
            .comments(List.of())
            .build();
    }

    @Test
    void should_GetTicket_When_RequesterIsTheTicketRequester()
    {
        User requester = buildUser("user-123", Role.REQUESTER);

        Ticket ticket = buildTicket(
            requester,
            buildUser("technician-123", Role.TECHNICIAN)
        );

        GetTicketInput input = new GetTicketInput(
            "ticket-123",
            requester
        );

        when(ticketRepository.findById(input.ticketId()))
            .thenReturn(Optional.of(ticket));

        Ticket result = getTicketUseCase.execute(input);

        assertThat(result).isNotNull();

        assertThat(result).isEqualTo(ticket);

        verify(ticketRepository).findById(input.ticketId());
    }

    @Test
    void should_GetTicket_When_TechnicianIsTheTicketAssignee()
    {
        User technician = buildUser(
            "technician-123",
            Role.TECHNICIAN
        );

        Ticket ticket = buildTicket(
            buildUser("user-123", Role.REQUESTER),
            technician
        );

        GetTicketInput input = new GetTicketInput(
            "ticket-123",
            technician
        );

        when(ticketRepository.findById(input.ticketId()))
            .thenReturn(Optional.of(ticket));

        Ticket result = getTicketUseCase.execute(input);

        assertThat(result).isNotNull();

        assertThat(result).isEqualTo(ticket);

        verify(ticketRepository).findById(input.ticketId());
    }

    @Test
    void should_GetTicket_When_UserIsAdmin()
    {
        User admin = buildUser(
            "admin-123",
            Role.ADMIN
        );

        Ticket ticket = buildTicket(
            buildUser("user-123", Role.REQUESTER),
            buildUser("technician-123", Role.TECHNICIAN)
        );

        GetTicketInput input = new GetTicketInput(
            "ticket-123",
            admin
        );

        when(ticketRepository.findById(input.ticketId()))
            .thenReturn(Optional.of(ticket));

        Ticket result = getTicketUseCase.execute(input);

        assertThat(result).isNotNull();

        assertThat(result).isEqualTo(ticket);

        verify(ticketRepository).findById(input.ticketId());
    }

    @Test
    void should_Throw_When_TicketDoesNotExist()
    {
        GetTicketInput input = new GetTicketInput(
            "ticket-123",
            buildUser("user-123", Role.REQUESTER)
        );

        when(ticketRepository.findById(input.ticketId()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> getTicketUseCase.execute(input))
            .isInstanceOf(ResourceNotFound.class);

        verify(ticketRepository).findById(input.ticketId());
    }

    @Test
    void should_Throw_When_RequesterIsNotTheTicketRequester()
    {
        User requester = buildUser("user-123", Role.REQUESTER);

        Ticket ticket = buildTicket(
            buildUser("another-user-123", Role.REQUESTER),
            buildUser("technician-123", Role.TECHNICIAN)
        );

        GetTicketInput input = new GetTicketInput(
            "ticket-123",
            requester
        );

        when(ticketRepository.findById(input.ticketId()))
            .thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> getTicketUseCase.execute(input))
            .isInstanceOf(ResourceNotFound.class);

        verify(ticketRepository).findById(input.ticketId());
    }

    @Test
    void should_Throw_When_TechnicianIsNotTheTicketAssignee()
    {
        User technician = buildUser(
            "technician-123",
            Role.TECHNICIAN
        );

        Ticket ticket = buildTicket(
            buildUser("user-123", Role.REQUESTER),
            buildUser("another-technician-123", Role.TECHNICIAN)
        );

        GetTicketInput input = new GetTicketInput(
            "ticket-123",
            technician
        );

        when(ticketRepository.findById(input.ticketId()))
            .thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> getTicketUseCase.execute(input))
            .isInstanceOf(ResourceNotFound.class);

        verify(ticketRepository).findById(input.ticketId());
    }
}
