package com.ticketSystem.infra.data.user;

import java.util.Optional;

import com.ticketSystem.application.port.data.user.UserRepository;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.data.user.jpa.repository.UserJpaRepository;

public final class DefaultUserRepository implements UserRepository
{
    private final UserJpaRepository jpaRepository;
    private final UserJpaMapper mapper;

    public DefaultUserRepository(UserJpaRepository jpaRepository, UserJpaMapper mapper)
    {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(User user)
    {
        this.jpaRepository.save(this.mapper.toJpa(user));
    }

    @Override
    public boolean existsByEmail(String email)
    {
        return this.jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(String id)
    {
        return this.jpaRepository.findById(this.mapper.toJpaId(id)).map(this.mapper::toEntity);
    }

    @Override
    public Optional<User> findByEmail(String email)
    {
        return this.jpaRepository.findByEmail(email).map(this.mapper::toEntity);
    }
}
