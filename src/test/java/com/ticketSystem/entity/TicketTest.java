package com.ticketSystem.entity;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ticketSystem.domain.ticket.Category;
import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.domain.ticket.Priority;
import com.ticketSystem.domain.ticket.Status;
import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.ticket.TicketBuilder;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;

class TicketTest
{
    User createUser()
    {
        return UserBuilder.of()
            .id("user-123")
            .name("John Doe")
            .email("john.doe@example.com")
            .role(Role.REQUESTER)
            .department(Department.IT)
            .build();
    }

    Ticket createTicket(
        User requester,
        User assignee,
        Status status,
        LocalDateTime closedAt,
        LocalDateTime createdAt
    ) {
        return TicketBuilder.of()
            .id("123")
            .title("Computer does not start")
            .description("The computer does not turn on.")
            .status(status)
            .priority(Priority.HIGH)
            .assignee(assignee)
            .closedAt(closedAt)
            .requester(requester)
            .category(Category.HARDWARE)
            .createdAt(createdAt)
            .comments(List.of())
            .build();
    }

    Ticket createClosedTicket()
    {
        return createTicket(
            createUser(),
            createUser(),
            Status.CLOSED,
            CREATED_AT.plusHours(1),
            CREATED_AT
        );
    }

    final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 8, 24, 10, 0);

    final Clock CLOCK = Clock.fixed(
        CREATED_AT.plusHours(1).toInstant(ZoneOffset.UTC),
        ZoneOffset.UTC
    );

    @Test
    void should_CreateTicket_When_ValidDataIsProvided()
    {
        User requester = createUser();
        User assignee = createUser();

        Ticket ticket = createTicket(
            requester,
            assignee,
            Status.OPEN,
            null,
            CREATED_AT
        );

        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isEqualTo("123");
        assertThat(ticket.getTitle()).isEqualTo("Computer does not start");
        assertThat(ticket.getDescription()).isEqualTo("The computer does not turn on.");
        assertThat(ticket.getStatus()).isEqualTo(Status.OPEN);
        assertThat(ticket.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(ticket.getAssignee()).isEqualTo(assignee);
        assertThat(ticket.getClosedAt()).isNull();
        assertThat(ticket.getRequester()).isEqualTo(requester);
        assertThat(ticket.getCategory()).isEqualTo(Category.HARDWARE);
        assertThat(ticket.getCreatedAt()).isEqualTo(CREATED_AT);
    }

    @Test
    void should_Throw_When_IdIsNull()
    {
        assertThatThrownBy(() ->
            TicketBuilder.of()
                .id(null)
                .title("Computer does not start")
                .description("The computer does not turn on.")
                .status(Status.OPEN)
                .priority(Priority.HIGH)
                .requester(createUser())
                .category(Category.HARDWARE)
                .createdAt(CREATED_AT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_TitleIsNull()
    {
        assertThatThrownBy(() ->
            TicketBuilder.of()
                .id("123")
                .title(null)
                .description("The computer does not turn on.")
                .status(Status.OPEN)
                .priority(Priority.HIGH)
                .requester(createUser())
                .category(Category.HARDWARE)
                .createdAt(CREATED_AT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_DescriptionIsNull()
    {
        assertThatThrownBy(() ->
            TicketBuilder.of()
                .id("123")
                .title("Computer does not start")
                .description(null)
                .status(Status.OPEN)
                .priority(Priority.HIGH)
                .requester(createUser())
                .category(Category.HARDWARE)
                .createdAt(CREATED_AT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_StatusIsNull()
    {
        assertThatThrownBy(() ->
            TicketBuilder.of()
                .id("123")
                .title("Computer does not start")
                .description("The computer does not turn on.")
                .status(null)
                .priority(Priority.HIGH)
                .requester(createUser())
                .category(Category.HARDWARE)
                .createdAt(CREATED_AT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_PriorityIsNull()
    {
        assertThatThrownBy(() ->
            TicketBuilder.of()
                .id("123")
                .title("Computer does not start")
                .description("The computer does not turn on.")
                .status(Status.OPEN)
                .priority(null)
                .requester(createUser())
                .category(Category.HARDWARE)
                .createdAt(CREATED_AT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_RequesterIsNull()
    {
        assertThatThrownBy(() ->
            TicketBuilder.of()
                .id("123")
                .title("Computer does not start")
                .description("The computer does not turn on.")
                .status(Status.OPEN)
                .priority(Priority.HIGH)
                .requester(null)
                .category(Category.HARDWARE)
                .createdAt(CREATED_AT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_CategoryIsNull()
    {
        assertThatThrownBy(() ->
            TicketBuilder.of()
                .id("123")
                .title("Computer does not start")
                .description("The computer does not turn on.")
                .status(Status.OPEN)
                .priority(Priority.HIGH)
                .requester(createUser())
                .category(null)
                .createdAt(CREATED_AT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_CreatedAtIsNull()
    {
        assertThatThrownBy(() ->
            TicketBuilder.of()
                .id("123")
                .title("Computer does not start")
                .description("The computer does not turn on.")
                .status(Status.OPEN)
                .priority(Priority.HIGH)
                .requester(createUser())
                .category(Category.HARDWARE)
                .createdAt(null)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_ClosedTicketHasNoClosedAt()
    {
        assertThatThrownBy(() ->
            createTicket(
                createUser(),
                createUser(),
                Status.CLOSED,
                null,
                CREATED_AT
            )
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void should_Throw_When_OpenTicketHasClosedAt()
    {
        assertThatThrownBy(() ->
            createTicket(
                createUser(),
                createUser(),
                Status.OPEN,
                CREATED_AT.plusHours(1),
                CREATED_AT
            )
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void should_Throw_When_ClosedTicketHasNoAssignee()
    {
        assertThatThrownBy(() ->
            createTicket(
                createUser(),
                null,
                Status.CLOSED,
                CREATED_AT.plusHours(1),
                CREATED_AT
            )
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void should_Throw_When_ClosedAtIsBeforeCreatedAt()
    {
        assertThatThrownBy(() ->
            createTicket(
                createUser(),
                createUser(),
                Status.CLOSED,
                CREATED_AT.minusHours(1),
                CREATED_AT
            )
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void should_AssignTicket_When_TicketIsOpen()
    {
        User requester = createUser();
        User assignee = createUser();

        Ticket ticket = createTicket(
            requester,
            null,
            Status.OPEN,
            null,
            CREATED_AT
        );

        ticket.assignTo(assignee);

        assertThat(ticket.getAssignee()).isEqualTo(assignee);
    }

    @Test
    void should_Throw_When_AssigningNullUser()
    {
        Ticket ticket = createTicket(
            createUser(),
            null,
            Status.OPEN,
            null,
            CREATED_AT
        );

        assertThatThrownBy(() -> ticket.assignTo(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_AssigningClosedTicket()
    {
        Ticket ticket = createClosedTicket();

        assertThatThrownBy(() -> ticket.assignTo(createUser()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Cannot assign a closed ticket.");
    }

    @Test
    void should_UnassignTicket_When_TicketIsAssigned()
    {
        User assignee = createUser();

        Ticket ticket = createTicket(
            createUser(),
            assignee,
            Status.OPEN,
            null,
            CREATED_AT
        );

        ticket.unassign();

        assertThat(ticket.getAssignee()).isNull();
    }

    @Test
    void should_Throw_When_UnassigningUnassignedTicket()
    {
        Ticket ticket = createTicket(
            createUser(),
            null,
            Status.OPEN,
            null,
            CREATED_AT
        );

        assertThatThrownBy(ticket::unassign)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Ticket is not assigned to anyone.");
    }

    @Test
    void should_Throw_When_UnassigningClosedTicket()
    {
        Ticket ticket = createClosedTicket();

        assertThatThrownBy(ticket::unassign)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Cannot unassign a closed ticket.");
    }

    @Test
    void should_CloseTicket_When_TicketIsAssigned()
    {
        Ticket ticket = createTicket(
            createUser(),
            createUser(),
            Status.OPEN,
            null,
            CREATED_AT
        );

        ticket.close(CLOCK);

        assertThat(ticket.isClosed()).isTrue();
        assertThat(ticket.getStatus()).isEqualTo(Status.CLOSED);
        assertThat(ticket.getClosedAt()).isEqualTo(CREATED_AT.plusHours(1));
    }

    @Test
    void should_Throw_When_ClosingUnassignedTicket()
    {
        Ticket ticket = createTicket(
            createUser(),
            null,
            Status.OPEN,
            null,
            CREATED_AT
        );

        assertThatThrownBy(() -> ticket.close(CLOCK))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Cannot close a ticket without an assignee.");
    }

    @Test
    void should_Throw_When_ClosingAlreadyClosedTicket()
    {
        Ticket ticket = createClosedTicket();

        assertThatThrownBy(() -> ticket.close(CLOCK))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Ticket is already closed.");
    }

    @Test
    void should_Throw_When_ClosingWithClockBeforeCreation()
    {
        Clock clock = Clock.fixed(
            CREATED_AT.minusHours(1).toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        );

        Ticket ticket = createTicket(
            createUser(),
            createUser(),
            Status.OPEN,
            null,
            CREATED_AT
        );

        assertThatThrownBy(() -> ticket.close(clock))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("ClosedAt timestamp cannot be before CreatedAt timestamp.");
    }

    @Test
    void should_ReturnDeadline_BasedOnPriorityAndCreationDate()
    {
        Ticket ticket = createTicket(
            createUser(),
            null,
            Status.OPEN,
            null,
            CREATED_AT
        );

        assertThat(ticket.getDeadline())
            .isEqualTo(CREATED_AT.plus(ticket.getPriority().getDuration()));
    }

    @Test
    void should_ReturnTrue_When_TicketIsClosed()
    {
        Ticket ticket = createClosedTicket();

        assertThat(ticket.isClosed()).isTrue();
    }

    @Test
    void should_ReturnFalse_When_TicketIsOpen()
    {
        Ticket ticket = createTicket(
            createUser(),
            null,
            Status.OPEN,
            null,
            CREATED_AT
        );

        assertThat(ticket.isClosed()).isFalse();
    }

    @Test
    void should_addComment_When_ValidCommentIsProvided()
    {
        Ticket ticket = createTicket(
            createUser(),
            null,
            Status.OPEN,
            null,
            CREATED_AT
        );

        Comment comment = new Comment(
            "123",
            createUser(),
            "This is a comment.",
            LocalDateTime.now(CLOCK)
        );

        ticket.addComment(comment);

        assertThat(ticket.getComments()).containsExactly(comment);
    }
}
