package com.ticketSystem.infra.data.ticket.jpa.mapper;

import com.ticketSystem.domain.ticket.Comment;
import com.ticketSystem.infra.data.core.JpaMapper;
import com.ticketSystem.infra.data.ticket.jpa.entity.CommentJpa;
import com.ticketSystem.infra.data.user.jpa.mapper.UserJpaMapper;
import com.ticketSystem.infra.id.UlidManager;

public final class CommentJpaMapper extends JpaMapper
{
    private final UserJpaMapper userJpaMapper;

    public CommentJpaMapper(UlidManager ulidManager, UserJpaMapper userJpaMapper)
    {
        super(ulidManager);
        this.userJpaMapper = userJpaMapper;
    }

    public CommentJpa toJpa(Comment comment, String ticketId)
    {
        return new CommentJpa(
            super.toJpaId(comment.getId()),
            super.toJpaId(ticketId),
            userJpaMapper.toJpa(comment.getAuthor()),
            comment.getContent(),
            comment.getCreatedAt()
        );
    }

    public Comment toEntity(CommentJpa jpa)
    {
        return new Comment(
            this.toEntityId(jpa.getId()),
            userJpaMapper.toEntity(jpa.getAuthor()),
            jpa.getContent(),
            jpa.getCreatedAt()
        );
    }
}
