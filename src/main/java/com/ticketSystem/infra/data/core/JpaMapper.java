package com.ticketSystem.infra.data.core;

import com.ticketSystem.infra.id.UlidManager;

public abstract class JpaMapper
{
    private final UlidManager ulidManager;

    public JpaMapper(UlidManager ulidManager)
    {
        this.ulidManager = ulidManager;
    }

    public byte[] toJpaId(String id)
    {
        return this.ulidManager.toBytes(id);
    }

    public String toEntityId(byte[] jpaId)
    {
        return this.ulidManager.toString(jpaId);
    }
}
