package com.ticketSystem.application.usecase.ticket.get;

import com.ticketSystem.domain.user.User;

public record GetTicketInput(
    String ticketId,
    User authenticatedUser
) {

}
