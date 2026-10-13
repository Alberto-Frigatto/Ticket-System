package com.ticketSystem.infra.data.auth.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "user_credentials")
public final class UserCredentialsJpa
{
    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "user_id", nullable = false, updatable = false, length = 16)
    private byte[] userId;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    protected UserCredentialsJpa() {}

    public UserCredentialsJpa(byte[] userId, String hashedPassword)
    {
        this.userId = userId;
        this.hashedPassword = hashedPassword;
    }

    public byte[] getUserId()
    {
        return userId;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }
}
