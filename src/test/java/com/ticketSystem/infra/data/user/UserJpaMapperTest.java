package com.ticketSystem.infra.data.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;
import com.ticketSystem.infra.data.user.jpa.entity.UserJpa;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.id.UlidManager;

public class UserJpaMapperTest
{
    @Mock UlidManager ulidManager;

    UserJpaMapper mapper;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

        mapper = new UserJpaMapper(ulidManager);
    }

    @Test
    void should_MapAUserToUserJpa_When_UserIsValid()
    {
        User user = UserBuilder.of()
            .id("01M2ZT9N8NJ7R06TY952QEXGHZ")
            .name("Alberto")
            .email("alberto@email.com")
            .role(Role.REQUESTER)
            .department(Department.HR)
            .build();

        byte[] jpaId = new byte[16];

        when(ulidManager.toBytes(user.getId())).thenReturn(jpaId);

        UserJpa jpa = mapper.toJpa(user);

        assertThat(jpa).isNotNull();
        assertThat(jpa.getId()).isEqualTo(jpaId);
        assertThat(jpa.getDepartment()).isEqualTo(user.getDepartment());
        assertThat(jpa.getEmail()).isEqualTo(user.getEmail());
        assertThat(jpa.getName()).isEqualTo(user.getName());
        assertThat(jpa.getRole()).isEqualTo(user.getRole());
    }

    @Test
    void should_MapAUserJpaToUser_When_UserIsValid()
    {
        UserJpa userJpa = new UserJpa(
            new byte[16],
            "Alberto",
            "alberto@email.com",
            Role.REQUESTER,
            Department.HR
        );

        String entityId = "01M2ZT9N8NJ7R06TY952QEXGHZ";

        when(ulidManager.toString(userJpa.getId())).thenReturn(entityId);

        User entity = mapper.toEntity(userJpa);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(entityId);
        assertThat(entity.getDepartment()).isEqualTo(userJpa.getDepartment());
        assertThat(entity.getEmail()).isEqualTo(userJpa.getEmail());
        assertThat(entity.getName()).isEqualTo(userJpa.getName());
        assertThat(entity.getRole()).isEqualTo(userJpa.getRole());
    }
}
