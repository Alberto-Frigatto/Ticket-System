package com.ticketSystem.infra.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ticketSystem.application.port.security.PasswordManager;

public final class BCryptPasswordManager implements PasswordManager
{
    private final PasswordEncoder passwordEncoder;
    private static final int STRENGTH = 12;

    public BCryptPasswordManager()
    {
        this.passwordEncoder = new BCryptPasswordEncoder(STRENGTH);
    }

    @Override
    public String hash(String password)
    {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean verify(String plainPassword, String hashedPassword)
    {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
