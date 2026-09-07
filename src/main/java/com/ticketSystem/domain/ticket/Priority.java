package com.ticketSystem.domain.ticket;

import java.time.Duration;

public enum Priority
{
    LOW(Duration.ofHours(24)),
    MEDIUM(Duration.ofHours(12)),
    HIGH(Duration.ofHours(6));

    private final Duration duration;

    Priority(Duration deadline)
    {
        this.duration = deadline;
    }

    public Duration getDuration()
    {
        return duration;
    }
}
