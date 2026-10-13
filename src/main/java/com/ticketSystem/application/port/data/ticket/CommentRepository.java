package com.ticketSystem.application.port.data.ticket;

import java.util.List;

import com.ticketSystem.domain.ticket.Comment;

public interface CommentRepository
{
    void save(String ticketId, Comment comment);
    List<Comment> findAllByTicketId(String ticketId);
}
