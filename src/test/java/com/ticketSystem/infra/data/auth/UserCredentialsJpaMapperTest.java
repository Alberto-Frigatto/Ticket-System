package com.ticketSystem.infra.data.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.auth.UserCredentialsBuilder;
import com.ticketSystem.infra.data.auth.jpa.entity.UserCredentialsJpa;
import com.ticketSystem.infra.data.auth.jpa.mapper.UserCredentialsJpaMapper;
import com.ticketSystem.infra.id.UlidManager;

public class UserCredentialsJpaMapperTest
{
    @Mock UlidManager ulidManager;

    UserCredentialsJpaMapper mapper;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

        mapper = new UserCredentialsJpaMapper(ulidManager);
    }

    @Test
    void should_MapAUserCredentialsToUserCredentialsJpa_When_UserCredentialsIsValid()
    {
        UserCredentials credentials = UserCredentialsBuilder.of()
            .userId("01M2ZT9N8NJ7R06TY952QEXGHZ")
            .hashedPassword("hashedPassword")
            .build();

        byte[] jpaId = new byte[16];

        when(ulidManager.toBytes(credentials.getUserId())).thenReturn(jpaId);

        UserCredentialsJpa jpa = mapper.toJpa(credentials);

        assertThat(jpa).isNotNull();
        assertThat(jpa.getUserId()).isEqualTo(jpaId);
        assertThat(jpa.getHashedPassword()).isEqualTo(credentials.getHashedPassword());
    }

    @Test
    void should_MapAUserCredentialsJpaToUserCredentials_When_UserCredentialsJpaIsValid()
    {
        byte[] jpaId = new byte[16];

        UserCredentialsJpa jpa = new UserCredentialsJpa(
            jpaId,
            "hashedPassword"
        );

        when(ulidManager.toString(jpa.getUserId())).thenReturn("01M2ZT9N8NJ7R06TY952QEXGHZ");

        UserCredentials credentials = mapper.toEntity(jpa);

        assertThat(credentials).isNotNull();
        assertThat(credentials.getUserId()).isEqualTo("01M2ZT9N8NJ7R06TY952QEXGHZ");
        assertThat(credentials.getHashedPassword()).isEqualTo(jpa.getHashedPassword());
    }
}
