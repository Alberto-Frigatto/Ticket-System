package com.ticketSystem.infra.data.user.jpa.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.ticketSystem.infra.data.user.jpa.entity.UserJpa;

public interface UserJpaRepository extends Repository<UserJpa, byte[]>
{
    void save(UserJpa user);
    Optional<UserJpa> findById(byte[] id);
    Optional<UserJpa> findByEmail(String email);
    boolean existsByEmail(String email);
}
