package com.ticketSystem.infra.data.user.jpa.mapper;

import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;
import com.ticketSystem.infra.data.core.JpaMapper;
import com.ticketSystem.infra.data.user.jpa.entity.UserJpa;
import com.ticketSystem.infra.id.UlidManager;

public final class UserJpaMapper extends JpaMapper
{
    public UserJpaMapper(UlidManager ulidManager)
    {
        super(ulidManager);
    }

    public User toEntity(UserJpa jpa)
    {
        return UserBuilder.of()
            .id(super.toEntityId(jpa.getId()))
            .name(jpa.getName())
            .email(jpa.getEmail())
            .role(jpa.getRole())
            .department(jpa.getDepartment())
            .build();
    }

    public UserJpa toJpa(User entity)
    {
        return new UserJpa(
            super.toJpaId(entity.getId()),
            entity.getName(),
            entity.getEmail(),
            entity.getRole(),
            entity.getDepartment()
        );
    }
}
