package com.ticketSystem.application.usecase.ticket.create;

import com.ticketSystem.domain.ticket.Category;
import com.ticketSystem.domain.ticket.Priority;
import com.ticketSystem.domain.user.User;

public record CreateTicketInput(
    String title,
    String description,
    Priority priority,
    User authenticatedRequester,
    Category category
) {

}
