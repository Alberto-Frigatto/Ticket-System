package com.ticketSystem.infra.data.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.auth.UserCredentialsBuilder;
import com.ticketSystem.infra.data.auth.jpa.entity.UserCredentialsJpa;
import com.ticketSystem.infra.data.auth.jpa.mapper.UserCredentialsJpaMapper;
import com.ticketSystem.infra.data.auth.jpa.repository.UserCredentialsJpaRepository;
import com.ticketSystem.infra.id.UlidManager;

@DataJpaTest
@Testcontainers
class DefaultUserCredentialsRepositoryIT
{
    @SuppressWarnings("resource")
    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
        .withDatabaseName("ticket_system")
        .withUsername("test")
        .withPassword("test");

    @Autowired
    UserCredentialsJpaRepository jpaRepository;

    DefaultUserCredentialsRepository repository;

    UserCredentialsJpaMapper mapper;

    @BeforeEach
    void setUp()
    {
        mapper = new UserCredentialsJpaMapper(new UlidManager());

        repository = new DefaultUserCredentialsRepository(
            jpaRepository,
            mapper
        );
    }

    UserCredentials buildValidCredentials()
    {
        return UserCredentialsBuilder.of()
            .userId("01M2ZT9N8NJ7R06TY952QEXGHZ")
            .hashedPassword("hashedPassword")
            .build();
    }

    @Test
    void should_PersistUserCredentials_When_UserCredentialsAreValid()
    {
        UserCredentials credentials = buildValidCredentials();

        repository.save(credentials);

        Optional<UserCredentialsJpa> result = jpaRepository.findByUserId(
            mapper.toJpaId(credentials.getUserId())
        );

        assertThat(result)
            .isPresent()
            .get()
            .satisfies(jpa -> {
                assertThat(jpa.getUserId())
                    .isEqualTo(mapper.toJpaId(credentials.getUserId()));

                assertThat(jpa.getHashedPassword())
                    .isEqualTo(credentials.getHashedPassword());
            });
    }

    @Test
    void should_ReturnUserCredentials_When_UserCredentialsExist()
    {
        UserCredentials credentials = buildValidCredentials();

        repository.save(credentials);

        Optional<UserCredentials> result = repository.findByUserId(
            credentials.getUserId()
        );

        assertThat(result)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(credentials);
    }

    @Test
    void should_ReturnEmpty_When_UserCredentialsDoNotExist()
    {
        Optional<UserCredentials> result = repository.findByUserId(
            "01M2ZT9N8NJ7R06TY952QEXGHZ"
        );

        assertThat(result).isEmpty();
    }
}
