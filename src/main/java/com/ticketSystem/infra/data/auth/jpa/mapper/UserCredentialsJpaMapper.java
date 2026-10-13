package com.ticketSystem.infra.data.auth.jpa.mapper;

import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.auth.UserCredentialsBuilder;
import com.ticketSystem.infra.data.auth.jpa.entity.UserCredentialsJpa;
import com.ticketSystem.infra.data.core.JpaMapper;
import com.ticketSystem.infra.id.UlidManager;

public final class UserCredentialsJpaMapper extends JpaMapper
{
    public UserCredentialsJpaMapper(UlidManager ulidManager)
    {
        super(ulidManager);
    }

    public UserCredentials toEntity(UserCredentialsJpa jpa)
    {
        return UserCredentialsBuilder.of()
            .userId(super.toEntityId(jpa.getUserId()))
            .hashedPassword(jpa.getHashedPassword())
            .build();
    }

    public UserCredentialsJpa toJpa(UserCredentials entity)
    {
        return new UserCredentialsJpa(
            super.toJpaId(entity.getUserId()),
            entity.getHashedPassword()
        );
    }
}
