package com.ticketSystem.infra.data.auth;

import java.util.Optional;

import com.ticketSystem.application.port.data.user.UserCredentialsRepository;
import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.infra.data.auth.jpa.mapper.UserCredentialsJpaMapper;
import com.ticketSystem.infra.data.auth.jpa.repository.UserCredentialsJpaRepository;

public final class DefaultUserCredentialsRepository implements UserCredentialsRepository
{
    private final UserCredentialsJpaRepository jpaRepository;
    private final UserCredentialsJpaMapper mapper;

    public DefaultUserCredentialsRepository(
        UserCredentialsJpaRepository jpaRepository,
        UserCredentialsJpaMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(UserCredentials userCredentials)
    {
        this.jpaRepository.save(this.mapper.toJpa(userCredentials));
    }

    @Override
    public Optional<UserCredentials> findByUserId(String userId)
    {
        return this.jpaRepository.findByUserId(this.mapper.toJpaId(userId))
            .map(this.mapper::toEntity);
    }
}
