package com.ticketSystem.infra.id;

import com.ticketSystem.application.port.id.IdGenerator;

import de.huxhorn.sulky.ulid.ULID;

public class UlidManager implements IdGenerator
{
    private static final ULID ulid = new ULID();

    @Override
    public String generateId()
    {
        return ulid.nextULID();
    }

    public byte[] toBytes(String id)
    {
        return ULID.parseULID(id).toBytes();
    }

    public String toString(byte[] jpaId)
    {
        return ULID.fromBytes(jpaId).toString();
    }
}
