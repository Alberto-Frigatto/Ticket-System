package com.ticketSystem.infra.data.ticket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ticketSystem.domain.ticket.Category;
import com.ticketSystem.domain.ticket.Priority;
import com.ticketSystem.domain.ticket.Status;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.ticket.TicketBuilder;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;
import com.ticketSystem.infra.data.ticket.jpa.entity.TicketJpa;
import com.ticketSystem.infra.data.ticket.jpa.mapper.CommentJpaMapper;
import com.ticketSystem.infra.data.ticket.jpa.mapper.TicketJpaMapper;
import com.ticketSystem.infra.data.user.jpa.entity.UserJpa;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.id.UlidManager;

public class TicketJpaMapperTest
{
    @Mock UlidManager ulidManager;
    @Mock UserJpaMapper userMapper;

    TicketJpaMapper ticketMapper;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

        ticketMapper = new TicketJpaMapper(
            ulidManager,
            userMapper,
            new CommentJpaMapper(ulidManager, new UserJpaMapper(ulidManager))
        );
    }

    User buildValidUser()
    {
        return UserBuilder.of()
            .id("01M2ZT9N8NJ7R06TY952QEXGHZ")
            .name("Alberto")
            .email("alberto@email.com")
            .role(Role.REQUESTER)
            .department(Department.HR)
            .build();
    }

    UserJpa buildValidUserJpa()
    {
        return new UserJpa(
            new byte[16],
            "Alberto",
            "alberto@email.com",
            Role.REQUESTER,
            Department.HR
        );
    }

    Ticket buildValidTicket(User requester)
    {
        return TicketBuilder.of()
            .id("01M2ZT9N8NJ7R06TY952QEXGHZ")
            .title("Computer does not start")
            .description("The computer does not turn on.")
            .status(Status.OPEN)
            .priority(Priority.HIGH)
            .requester(requester)
            .category(Category.HARDWARE)
            .createdAt(LocalDateTime.now())
            .comments(List.of())
            .build();
    }

    TicketJpa buildValidTicketJpa(UserJpa requester)
    {
        return new TicketJpa(
            new byte[16],
            requester,
            Category.HARDWARE,
            LocalDateTime.now(),
            "Computer does not start",
            "The computer does not turn on.",
            Status.OPEN,
            Priority.HIGH,
            null,
            null
        );
    }

    @Test
    void should_MapATicketToTicketJpa_When_TicketIsValid()
    {
        User user = buildValidUser();
        UserJpa userJpa = buildValidUserJpa();
        Ticket ticket = buildValidTicket(user);

        byte[] ticketJpaId = new byte[16];

        when(ulidManager.toBytes(ticket.getId())).thenReturn(ticketJpaId);
        when(userMapper.toJpa(user)).thenReturn(userJpa);

        TicketJpa jpa = ticketMapper.toJpa(ticket);

        assertThat(jpa.getId()).isEqualTo(ticketJpaId);
        assertThat(jpa.getAssignee()).isNull();
        assertThat(jpa.getCategory()).isEqualTo(ticket.getCategory());
        assertThat(jpa.getClosedAt()).isNull();
        assertThat(jpa.getCreatedAt()).isEqualTo(ticket.getCreatedAt());
        assertThat(jpa.getDescription()).isEqualTo(ticket.getDescription());
        assertThat(jpa.getPriority()).isEqualTo(ticket.getPriority());
        assertThat(jpa.getRequester()).isEqualTo(userJpa);
        assertThat(jpa.getStatus()).isEqualTo(ticket.getStatus());
        assertThat(jpa.getTitle()).isEqualTo(ticket.getTitle());
    }

    @Test
    void should_MapATicketJpaToTicket_When_TicketJpaIsValid()
    {
        User user = buildValidUser();
        UserJpa userJpa = buildValidUserJpa();
        TicketJpa ticketJpa = buildValidTicketJpa(userJpa);

        String entityId = "01M2ZT9N8NJ7R06TY952QEXGHZ";

        when(userMapper.toEntity(userJpa)).thenReturn(user);
        when(ulidManager.toString(ticketJpa.getId())).thenReturn(entityId);

        Ticket entity = ticketMapper.toEntity(ticketJpa, List.of());

        assertThat(entity.getAssignee()).isNull();
        assertThat(entity.getCategory()).isEqualTo(ticketJpa.getCategory());
        assertThat(entity.getClosedAt()).isNull();
        assertThat(entity.getCreatedAt()).isEqualTo(ticketJpa.getCreatedAt());
        assertThat(entity.getDescription()).isEqualTo(ticketJpa.getDescription());
        assertThat(entity.getPriority()).isEqualTo(ticketJpa.getPriority());
        assertThat(entity.getRequester()).isEqualTo(user);
        assertThat(entity.getStatus()).isEqualTo(ticketJpa.getStatus());
        assertThat(entity.getTitle()).isEqualTo(ticketJpa.getTitle());
        assertThat(entity.getComments()).isEmpty();
    }
}
