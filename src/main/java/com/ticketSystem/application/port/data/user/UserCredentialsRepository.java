package com.ticketSystem.application.port.data.user;

import java.util.Optional;

import com.ticketSystem.domain.auth.UserCredentials;

public interface UserCredentialsRepository
{
    void save(UserCredentials userCredentials);
    Optional<UserCredentials> findByUserId(String userId);
}
