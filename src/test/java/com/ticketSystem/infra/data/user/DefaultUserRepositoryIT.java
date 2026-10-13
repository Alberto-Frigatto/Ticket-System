package com.ticketSystem.infra.data.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;

import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;
import com.ticketSystem.infra.data.user.jpa.entity.UserJpa;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.data.user.jpa.repository.UserJpaRepository;
import com.ticketSystem.infra.id.UlidManager;

@DataJpaTest
@Testcontainers
class DefaultUserRepositoryIT
{
    @SuppressWarnings("resource")
    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
        .withDatabaseName("ticket_system")
        .withUsername("test")
        .withPassword("test");

    @Autowired UserJpaRepository jpaRepository;

    DefaultUserRepository repository;
    UserJpaMapper mapper;

    @BeforeEach
    void setUp()
    {
        mapper = new UserJpaMapper(new UlidManager());
        repository = new DefaultUserRepository(jpaRepository, mapper);
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

    @Test
    void should_PersistUser_When_UserIsValid()
    {
        User user = buildValidUser();

        repository.save(user);

        Optional<UserJpa> result = jpaRepository.findById(
            mapper.toJpaId(user.getId())
        );

        assertThat(result)
            .isPresent()
            .get()
            .satisfies(jpa -> {
                assertThat(jpa.getName()).isEqualTo(user.getName());
                assertThat(jpa.getEmail()).isEqualTo(user.getEmail());
                assertThat(jpa.getRole()).isEqualTo(user.getRole());
                assertThat(jpa.getDepartment()).isEqualTo(user.getDepartment());
            });
    }

    @Test
    void should_ReturnTrue_When_UserExistsByEmail()
    {
        User user = buildValidUser();

        repository.save(user);

        boolean exists = repository.existsByEmail(user.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    void should_ReturnFalse_When_UserDoesNotExistByEmail()
    {
        boolean exists = repository.existsByEmail("nonexistent@email.com");

        assertThat(exists).isFalse();
    }

    @Test
    void should_ReturnUser_When_UserExistsById()
    {
        User user = buildValidUser();

        repository.save(user);

        Optional<User> result = repository.findById(user.getId());

        assertThat(result)
            .isPresent();

        assertThat(result)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(user);
    }

    @Test
    void should_ReturnEmpty_When_UserDoesNotExistById()
    {
        Optional<User> result = repository.findById(
            "01M2ZT9N8NJ7R06TY952QEXGHZ"
        );

        assertThat(result).isEmpty();
    }

    @Test
    void should_ReturnUser_When_UserExistsByEmail()
    {
        User user = buildValidUser();

        repository.save(user);

        Optional<User> result = repository.findByEmail(user.getEmail());

        assertThat(result)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(user);
    }

    @Test
    void should_ReturnEmpty_When_UserDoesNotExistByEmail()
    {
        Optional<User> result = repository.findByEmail(
            "nonexistent@email.com"
        );

        assertThat(result).isEmpty();
    }
}
