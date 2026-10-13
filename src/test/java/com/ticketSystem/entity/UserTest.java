package com.ticketSystem.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;

public class UserTest
{
    User buildValidUser()

    {
        return UserBuilder.of()
            .id("123")
            .name("John Doe")
            .email("john.doe@example.com")
            .role(Role.REQUESTER)
            .department(Department.IT)
            .build();
    }

    @Test
    void should_CreateUser_When_ValidDataIsProvided()
    {
        User user = buildValidUser();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo("123");
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(user.getRole()).isEqualTo(Role.REQUESTER);
        assertThat(user.getDepartment()).isEqualTo(Department.IT);
    }

    @Test
    void should_Throw_When_IdIsNull()
    {
        assertThatThrownBy(() ->
            UserBuilder.of()
                .id(null)
                .name("John Doe")
                .email("john.doe@example.com")
                .role(Role.REQUESTER)
                .department(Department.IT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_NameIsNull()
    {
        assertThatThrownBy(() ->
            UserBuilder.of()
                .id("123")
                .name(null)
                .email("john.doe@example.com")
                .role(Role.REQUESTER)
                .department(Department.IT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_EmailIsNull()
    {
        assertThatThrownBy(() ->
            UserBuilder.of()
                .id("123")
                .name("John Doe")
                .email(null)
                .role(Role.REQUESTER)
                .department(Department.IT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_RoleIsNull()
    {
        assertThatThrownBy(() ->
            UserBuilder.of()
                .id("123")
                .name("John Doe")
                .email("john.doe@example.com")
                .role(null)
                .department(Department.IT)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_Throw_When_DepartmentIsNull()
    {
        assertThatThrownBy(() ->
            UserBuilder.of()
                .id("123")
                .name("John Doe")
                .email("john.doe@example.com")
                .role(Role.REQUESTER)
                .department(null)
                .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_DefineCredentials_When_ValidHashedPasswordIsProvided()
    {
        User user = buildValidUser();

        UserCredentials credentials = user.defineCredentials("hashed-password");

        assertThat(credentials).isNotNull();
        assertThat(credentials.getUserId()).isEqualTo("123");
        assertThat(credentials.getHashedPassword()).isEqualTo("hashed-password");
    }

    @Test
    void should_Throw_When_HashedPasswordIsNull()
    {
        User user = buildValidUser();

        assertThatThrownBy(() -> user.defineCredentials(null))
            .isInstanceOf(NullPointerException.class);
    }
}
