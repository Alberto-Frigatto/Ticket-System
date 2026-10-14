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

import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.domain.ticket.Category;
import com.ticketSystem.domain.ticket.Priority;
import com.ticketSystem.domain.ticket.Status;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;
import com.ticketSystem.infra.data.ticket.jpa.entity.CommentJpa;
import com.ticketSystem.infra.data.ticket.jpa.entity.TicketJpa;
import com.ticketSystem.infra.data.ticket.jpa.mapper.CommentJpaMapper;
import com.ticketSystem.infra.data.ticket.jpa.repository.CommentJpaRepository;
import com.ticketSystem.infra.data.ticket.jpa.repository.TicketJpaRepository;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.data.user.jpa.repository.UserJpaRepository;
import com.ticketSystem.infra.id.UlidManager;

@DataJpaTest
@Testcontainers
class DefaultCommentRepositoryIT
{
    @SuppressWarnings("resource")
    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
        .withDatabaseName("ticket_system")
        .withUsername("test")
        .withPassword("test");

    @Autowired
    CommentJpaRepository jpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    TicketJpaRepository ticketJpaRepository;

    DefaultCommentRepository repository;

    CommentJpaMapper mapper;

    UserJpaMapper userJpaMapper;

    @BeforeEach
    void setUp() {
        UlidManager ulidManager = new UlidManager();

        mapper = new CommentJpaMapper(
            ulidManager,
            new UserJpaMapper(ulidManager)
        );

        repository = new DefaultCommentRepository(jpaRepository, mapper);

        userJpaMapper = new UserJpaMapper(ulidManager);
    }

    User buildValidUser() {
        return UserBuilder.of()
            .id("01M2ZT9N8NJ7R06TY952QEXGHZ")
            .name("Alberto")
            .email("alberto@email.com")
            .role(Role.REQUESTER)
            .department(Department.HR)
            .build();
    }

    Comment buildValidComment(User author) {
        return new Comment(
            "01M2ZT9N8NJ7R06TY952QEXGHZ",
            author,
            "Meu computador não está funcionando.",
            LocalDateTime.of(2026, 9, 30, 10, 0)
        );
    }

    void persistUser(User user) {
        userJpaRepository.save(userJpaMapper.toJpa(user));
    }

    void persistTicket(User requester, String ticketId) {
        ticketJpaRepository.save(new TicketJpa(
            mapper.toJpaId(ticketId),
            userJpaMapper.toJpa(requester),
            Category.HARDWARE,
            LocalDateTime.of(2026, 9, 30, 9, 0),
            "Computer issue",
            "The computer is not working.",
            Status.OPEN,
            Priority.MEDIUM,
            null,
            null
        ));
    }

    @Test
    void should_PersistComment_When_CommentIsValid() {
        User user = buildValidUser();
        Comment comment = buildValidComment(user);
        String ticketId = "01M2ZT9N8NJ7R06TY952QEXGHZ";

        persistUser(user);
        persistTicket(user, ticketId);

        repository.save(ticketId, comment);

        Optional<CommentJpa> result = jpaRepository.findById(
            mapper.toJpaId(comment.getId())
        );

        assertThat(result)
            .isPresent()
            .get()
            .satisfies(jpa -> {
                assertThat(jpa.getTicketId())
                    .isEqualTo(mapper.toJpaId(ticketId));

                assertThat(jpa.getContent())
                    .isEqualTo(comment.getContent());

                assertThat(jpa.getCreatedAt())
                    .isEqualTo(comment.getCreatedAt());

                assertThat(jpa.getAuthor().getId())
                    .isEqualTo(mapper.toJpaId(user.getId()));
            });
    }

    @Test
    void should_ReturnComments_When_TicketHasComments() {
        User user = buildValidUser();
        Comment comment = buildValidComment(user);
        String ticketId = "01M2ZT9N8NJ7R06TY952QEXGHZ";

        persistUser(user);
        persistTicket(user, ticketId);

        repository.save(ticketId, comment);

        List<Comment> result = repository.findAllByTicketId(ticketId);

        assertThat(result)
            .hasSize(1)
            .first()
            .usingRecursiveComparison()
            .isEqualTo(comment);
    }

    @Test
    void should_ReturnEmpty_When_TicketHasNoComments() {
        String ticketId = "01M2ZT9N8NJ7R06TY952QEXGHZ";

        List<Comment> result = repository.findAllByTicketId(ticketId);

        assertThat(result).isEmpty();
    }
}
