package com.ticketSystem.infra.data.ticket;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
import com.ticketSystem.infra.data.ticket.jpa.entity.TicketJpa;
import com.ticketSystem.infra.data.ticket.jpa.mapper.CommentJpaMapper;
import com.ticketSystem.infra.data.ticket.jpa.mapper.TicketJpaMapper;
import com.ticketSystem.infra.data.ticket.jpa.repository.CommentJpaRepository;
import com.ticketSystem.infra.data.ticket.jpa.repository.TicketJpaRepository;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.data.user.jpa.repository.UserJpaRepository;
import com.ticketSystem.infra.id.UlidManager;

@DataJpaTest
@Testcontainers
class DefaultTicketRepositoryIT
{
    @SuppressWarnings("resource")
    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
        .withDatabaseName("ticket_system")
        .withUsername("test")
        .withPassword("test");

    @Autowired
    TicketJpaRepository ticketJpaRepository;

    @Autowired
    CommentJpaRepository commentJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    DefaultTicketRepository repository;

    DefaultCommentRepository commentRepository;

    TicketJpaMapper mapper;

    CommentJpaMapper commentMapper;

    UserJpaMapper userMapper;

    @BeforeEach
    void setUp()
    {
        UlidManager ulidManager = new UlidManager();

        userMapper = new UserJpaMapper(ulidManager);

        commentMapper = new CommentJpaMapper(
            ulidManager,
            userMapper
        );

        mapper = new TicketJpaMapper(
            ulidManager,
            userMapper,
            commentMapper
        );

        repository = new DefaultTicketRepository(
            ticketJpaRepository,
            commentJpaRepository,
            mapper
        );

        commentRepository = new DefaultCommentRepository(
            commentJpaRepository,
            commentMapper
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

    Ticket buildValidTicket(User requester)
    {
        return TicketBuilder.of()
            .id("01M2ZT9N8NJ7R06TY952QEXGHZ")
            .requester(requester)
            .category(Category.HARDWARE)
            .createdAt(LocalDateTime.of(2026, 9, 30, 9, 0))
            .title("Computer issue")
            .description("The computer is not working.")
            .status(Status.OPEN)
            .priority(Priority.MEDIUM)
            .assignee(null)
            .closedAt(null)
            .comments(List.of())
            .build();
    }

    Comment buildValidComment(User author)
    {
        return new Comment(
            "01M4VSBZP7K6PGFZXAN5P5ZV2F",
            author,
            "Meu computador não está funcionando.",
            LocalDateTime.of(2026, 9, 30, 10, 0)
        );
    }

    void persistUser(User user)
    {
        userJpaRepository.save(userMapper.toJpa(user));
    }

    @Test
    void should_PersistTicket_When_TicketIsValid()
    {
        User requester = buildValidUser();
        Ticket ticket = buildValidTicket(requester);

        persistUser(requester);

        repository.save(ticket);

        Optional<TicketJpa> result = ticketJpaRepository.findById(
            mapper.toJpaId(ticket.getId())
        );

        assertThat(result)
            .isPresent()
            .get()
            .satisfies(jpa -> {
                assertThat(jpa.getId())
                    .isEqualTo(mapper.toJpaId(ticket.getId()));

                assertThat(jpa.getRequester().getId())
                    .isEqualTo(mapper.toJpaId(requester.getId()));

                assertThat(jpa.getCategory())
                    .isEqualTo(ticket.getCategory());

                assertThat(jpa.getCreatedAt())
                    .isEqualTo(ticket.getCreatedAt());

                assertThat(jpa.getTitle())
                    .isEqualTo(ticket.getTitle());

                assertThat(jpa.getDescription())
                    .isEqualTo(ticket.getDescription());

                assertThat(jpa.getStatus())
                    .isEqualTo(ticket.getStatus());

                assertThat(jpa.getPriority())
                    .isEqualTo(ticket.getPriority());

                assertThat(jpa.getAssignee()).isNull();
                assertThat(jpa.getClosedAt()).isNull();
            });
    }

    @Test
    void should_ReturnTicket_When_TicketExists()
    {
        User requester = buildValidUser();
        Ticket ticket = buildValidTicket(requester);

        persistUser(requester);
        repository.save(ticket);

        Optional<Ticket> result = repository.findById(ticket.getId());

        assertThat(result)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(ticket);
    }

    @Test
    void should_ReturnTicketWithComments_When_TicketHasComments()
    {
        User requester = buildValidUser();
        Ticket ticket = buildValidTicket(requester);
        Comment comment = buildValidComment(requester);

        persistUser(requester);
        repository.save(ticket);
        commentRepository.save(ticket.getId(), comment);

        Optional<Ticket> result = repository.findById(ticket.getId());

        assertThat(result)
            .isPresent()
            .get()
            .satisfies(found -> {
                assertThat(found.getId())
                    .isEqualTo(ticket.getId());

                assertThat(found.getComments())
                    .hasSize(1)
                    .first()
                    .usingRecursiveComparison()
                    .isEqualTo(comment);
            });
    }

    @Test
    void should_ReturnEmpty_When_TicketDoesNotExist()
    {
        Optional<Ticket> result = repository.findById(
            "01M2ZT9N8NJ7R06TY952QEXGHZ"
        );

        assertThat(result).isEmpty();
    }
}
