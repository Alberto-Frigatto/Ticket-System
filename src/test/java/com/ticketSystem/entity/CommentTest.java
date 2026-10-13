package com.ticketSystem.entity;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;

public class CommentTest
{
    User buildValidAuthor()
    {
        return UserBuilder.of()
            .id("user-123")
            .name("John Doe")
            .email("john.doe@example.com")
            .role(Role.REQUESTER)
            .department(Department.IT)
            .build();
    }

    Comment buildValidComment()
    {
        return new Comment(
            "comment-123",
            buildValidAuthor(),
            "The computer has been restarted.",
            LocalDateTime.of(2026, 9, 9, 15, 0)
        );
    }

    @Test
    void should_CreateComment_When_ValidDataIsProvided()
    {
        Comment comment = buildValidComment();

        assertThat(comment).isNotNull();

        assertThat(comment.getId()).isEqualTo("comment-123");

        assertThat(comment.getAuthor().getId()).isEqualTo(buildValidAuthor().getId());

        assertThat(comment.getContent())
            .isEqualTo("The computer has been restarted.");

        assertThat(comment.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2026, 9, 9, 15, 0));
    }

    @Test
    void should_Throw_When_IdIsNull()
    {
        assertThatThrownBy(() -> new Comment(
            null,
            buildValidAuthor(),
            "The computer has been restarted.",
            LocalDateTime.of(2026, 9, 9, 15, 0)
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_AuthorIsNull()
    {
        assertThatThrownBy(() -> new Comment(
            "comment-123",
            null,
            "The computer has been restarted.",
            LocalDateTime.of(2026, 9, 9, 15, 0)
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_ContentIsNull()
    {
        assertThatThrownBy(() -> new Comment(
            "comment-123",
            buildValidAuthor(),
            null,
            LocalDateTime.of(2026, 9, 9, 15, 0)
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_CreatedAtIsNull()
    {
        assertThatThrownBy(() -> new Comment(
            "comment-123",
            buildValidAuthor(),
            "The computer has been restarted.",
            null
        )).isInstanceOf(NullPointerException.class);
    }
}
