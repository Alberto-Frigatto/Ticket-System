package com.ticketSystem.application.usecase.ticket;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.ticketSystem.application.port.data.ticket.TicketRepository;
import com.ticketSystem.application.port.id.IdGenerator;
import com.ticketSystem.application.usecase.ticket.create.CreateTicketInput;
import com.ticketSystem.application.usecase.ticket.create.CreateTicketUseCase;
import com.ticketSystem.domain.ticket.Category;
import com.ticketSystem.domain.ticket.Priority;
import com.ticketSystem.domain.ticket.Status;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;

public class CreateTicketUseCaseTest
{

    @Mock IdGenerator idGenerator;
    @Mock TicketRepository ticketRepository;

    Clock clock;
    CreateTicketUseCase createTicketUseCase;

    @BeforeEach void setup()
    {
        MockitoAnnotations.openMocks(this);

        clock = Clock.fixed(
            Instant.parse("2026-09-09T15:00:00Z"),
            ZoneOffset.UTC
        );

        createTicketUseCase = new CreateTicketUseCase(
            idGenerator,
            ticketRepository,
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

    CreateTicketInput buildValidInput()
    {
        return new CreateTicketInput(
            "Computer does not start",
            "The computer does not turn on.",
            Priority.HIGH,
            buildValidRequester(),
            Category.HARDWARE
        );
    }

    @Test
    void should_CreateTicket_When_ValidDataIsProvided()
    {
        CreateTicketInput input = buildValidInput();

        when(idGenerator.generateId()).thenReturn("ticket-123");

        Ticket ticket = createTicketUseCase.execute(input);

        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isEqualTo("ticket-123");
        assertThat(ticket.getTitle()).isEqualTo("Computer does not start");
        assertThat(ticket.getDescription()).isEqualTo("The computer does not turn on.");
        assertThat(ticket.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(ticket.getCategory()).isEqualTo(Category.HARDWARE);
        assertThat(ticket.getRequester()).isEqualTo(input.authenticatedRequester());
        assertThat(ticket.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 9, 9, 15, 0));
        assertThat(ticket.getStatus()).isEqualTo(Status.OPEN);
        assertThat(ticket.getComments()).isEqualTo(List.of());

        verify(idGenerator).generateId();
        verify(ticketRepository).save(ticket);
    }
}
