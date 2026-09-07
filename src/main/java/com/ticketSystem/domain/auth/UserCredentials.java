package com.ticketSystem.domain.auth;

import java.util.Objects;

public final class UserCredentials
{
    private final String userId;
    private String hashedPassword;

    protected UserCredentials(
        String userId,
        String hashedPassword
    ) {
        this.userId = Objects.requireNonNull(userId);
        this.hashedPassword = Objects.requireNonNull(hashedPassword);
    }

    public String getUserId()
    {
        return userId;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }
}
