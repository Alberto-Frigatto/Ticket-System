package com.ticketSystem.infra.data.ticket.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.ticketSystem.infra.data.ticket.jpa.entity.CommentJpa;

public interface CommentJpaRepository extends Repository<CommentJpa, byte[]>
{
    void save(CommentJpa commentJpa);
    Optional<CommentJpa> findById(byte[] id);
    List<CommentJpa> findAllByTicketId(byte[] ticketId);
}
