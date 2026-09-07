package com.ticketSystem.domain.auth;

public class UserCredentialsBuilder
{
    private String hashedPassword;
    private String userId;

    private UserCredentialsBuilder() {}

    public static UserCredentialsBuilder of()
    {
        return new UserCredentialsBuilder();
    }

    public UserCredentialsBuilder hashedPassword(String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
        return this;
    }

    public UserCredentialsBuilder userId(String userId)
    {
        this.userId = userId;
        return this;
    }

    public UserCredentials build()
    {
        return new UserCredentials(
            userId,
            hashedPassword
        );
    }
}
