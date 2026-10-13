package com.ticketSystem.infra.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BCryptPasswordManagerTest
{
    private BCryptPasswordManager passwordManager;

    @BeforeEach
    void setUp()
    {
        passwordManager = new BCryptPasswordManager();
    }

    @Test
    void should_GenerateAHash_When_PasswordIsValid()
    {
        String password = "SecurePassword123";

        String hashedPassword = passwordManager.hash(password);

        assertThat(hashedPassword)
            .isNotNull()
            .isNotEqualTo(password)
            .startsWith("$2a$12$");
    }

    @Test
    void should_GenerateDifferentHashes_When_HashingTheSamePassword()
    {
        String password = "SecurePassword123";

        String firstHash = passwordManager.hash(password);
        String secondHash = passwordManager.hash(password);

        assertThat(secondHash).isNotEqualTo(firstHash);
    }

    @Test
    void should_ReturnTrue_When_PasswordMatchesHash()
    {
        String password = "SecurePassword123";
        String hashedPassword = passwordManager.hash(password);

        boolean matches = passwordManager.verify(password, hashedPassword);

        assertThat(matches).isTrue();
    }

    @Test
    void should_ReturnFalse_When_PasswordDoesNotMatchHash()
    {
        String password = "SecurePassword123";
        String hashedPassword = passwordManager.hash(password);

        boolean matches = passwordManager.verify(
            "DifferentPassword123",
            hashedPassword
        );

        assertThat(matches).isFalse();
    }

    @Test
    void should_ReturnFalse_When_PasswordIsEmptyAndHashBelongsToAnotherPassword()
    {
        String hashedPassword = passwordManager.hash("SecurePassword123");

        boolean matches = passwordManager.verify("", hashedPassword);

        assertThat(matches).isFalse();
    }
}
