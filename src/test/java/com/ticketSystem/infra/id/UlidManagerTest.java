package com.ticketSystem.infra.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UlidManagerTest
{
    private UlidManager ulidManager;

    @BeforeEach
    void setUp()
    {
        ulidManager = new UlidManager();
    }

    @Test
    void should_GenerateAValidUlid()
    {
        String id = ulidManager.generateId();

        assertThat(id)
            .isNotNull()
            .hasSize(26)
            .matches("[0-9A-HJKMNP-TV-Z]{26}");
    }

    @Test
    void should_GenerateDifferentIds_When_CalledConsecutively()
    {
        String firstId = ulidManager.generateId();
        String secondId = ulidManager.generateId();

        assertThat(secondId).isNotEqualTo(firstId);
    }

    @Test
    void should_ConvertUlidToBytes_When_IdIsValid()
    {
        String id = ulidManager.generateId();

        byte[] bytes = ulidManager.toBytes(id);

        assertThat(bytes).hasSize(16);
    }

    @Test
    void should_ConvertBytesToUlid_When_BytesAreValid()
    {
        String id = ulidManager.generateId();
        byte[] bytes = ulidManager.toBytes(id);

        String convertedId = ulidManager.toString(bytes);

        assertThat(convertedId).isEqualTo(id);
    }

    @Test
    void should_PreserveUlid_When_ConvertingBackAndForth()
    {
        String originalId = ulidManager.generateId();

        String convertedId = ulidManager.toString(
            ulidManager.toBytes(originalId)
        );

        byte[] convertedBytes = ulidManager.toBytes(convertedId);

        assertThat(convertedId).isEqualTo(originalId);
        assertThat(convertedBytes)
            .containsExactly(ulidManager.toBytes(originalId));
    }

    @Test
    void should_ThrowException_When_IdIsInvalid()
    {
        String invalidId = "invalid";

        assertThatThrownBy(() -> ulidManager.toBytes(invalidId))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
