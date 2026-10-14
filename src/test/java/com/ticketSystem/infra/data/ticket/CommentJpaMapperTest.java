package com.ticketSystem.infra.data.ticket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;
import com.ticketSystem.infra.data.ticket.jpa.entity.CommentJpa;
import com.ticketSystem.infra.data.ticket.jpa.mapper.CommentJpaMapper;
import com.ticketSystem.infra.data.user.jpa.entity.UserJpa;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.id.UlidManager;

public class CommentJpaMapperTest
{
    @Mock UlidManager ulidManager;
    @Mock UserJpaMapper userJpaMapper;

    CommentJpaMapper commentJpaMapper;
    Clock clock;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

        clock = Clock.fixed(
            Instant.parse("2026-09-09T15:00:00Z"),
            ZoneOffset.UTC
        );

        commentJpaMapper = new CommentJpaMapper(ulidManager, userJpaMapper);
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

    @Test
    void should_MapACommentToCommentJpa_When_CommentIsValid()
    {
        User author = buildValidUser();
        UserJpa authorJpa = buildValidUserJpa();

        LocalDateTime createdAt = LocalDateTime.now(clock);

        Comment comment = new Comment(
            "id-123",
            author,
            "Content",
            createdAt
        );

        String ticketId = "ticket-123";

        byte[] jpaId = new byte[16];
        byte[] jpaTicketId = new byte[16];

        when(ulidManager.toBytes(comment.getId())).thenReturn(jpaId);
        when(ulidManager.toBytes(ticketId)).thenReturn(jpaTicketId);
        when(userJpaMapper.toJpa(author)).thenReturn(authorJpa);

        CommentJpa jpa = commentJpaMapper.toJpa(comment, ticketId);

        assertThat(jpa).isNotNull();
        assertThat(jpa.getId()).isEqualTo(jpaId);
        assertThat(jpa.getAuthor()).isEqualTo(authorJpa);
        assertThat(jpa.getContent()).isEqualTo(comment.getContent());
        assertThat(jpa.getCreatedAt()).isEqualTo(createdAt);
        assertThat(jpa.getTicketId()).isEqualTo(jpaTicketId);

        verify(ulidManager).toBytes(comment.getId());
        verify(ulidManager).toBytes(ticketId);
        verify(userJpaMapper).toJpa(author);
    }
}
