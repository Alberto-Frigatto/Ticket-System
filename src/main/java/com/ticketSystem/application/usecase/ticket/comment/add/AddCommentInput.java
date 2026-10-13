package com.ticketSystem.application.usecase.ticket.comment.add;

import com.ticketSystem.domain.user.User;

public record AddCommentInput(
    String ticketId,
    String content,
    User authenticatedUser
) {

}
