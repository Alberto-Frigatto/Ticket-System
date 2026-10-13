package com.ticketSystem.infra.data.auth.jpa.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.ticketSystem.infra.data.auth.jpa.entity.UserCredentialsJpa;

public interface UserCredentialsJpaRepository extends Repository<UserCredentialsJpa, byte[]>
{
    void save(UserCredentialsJpa userCredentials);
    Optional<UserCredentialsJpa> findByUserId(byte[] userId);
}
