package com.ticketSystem.application.usecase.ticket.access;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import com.ticketSystem.domain.ticket.Ticket;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;

public final class TicketAccessStrategy
{
    private static final Map<Role, Function<User, Predicate<Ticket>>> STRATEGIES = Map.of(
        Role.REQUESTER, user -> ticket -> ticket.getRequester().getId().equals(user.getId()),

        Role.TECHNICIAN,
        user -> ticket ->
            ticket.getAssignee() != null
                && ticket.getAssignee().getId().equals(user.getId()),

        Role.ADMIN, user -> ticket -> true
    );

    private TicketAccessStrategy() {}

    public static Predicate<Ticket> getStrategy(User user)
    {
        Function<User, Predicate<Ticket>> strategy = STRATEGIES.get(user.getRole());

        if (strategy == null)
        {
            throw new IllegalArgumentException(
                "No ticket authorization strategy defined for role: " + user.getRole()
            );
        }

        return strategy.apply(user);
    }
}
