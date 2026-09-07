package com.ticketSystem.application.port.data.user;

import java.util.Optional;

import com.ticketSystem.domain.user.User;

public interface UserRepository
{
    void save(User user);
    boolean existsByEmail(String email);
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
}
