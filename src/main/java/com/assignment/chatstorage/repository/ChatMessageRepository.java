package com.assignment.chatstorage.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.assignment.chatstorage.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    Page<ChatMessageEntity> findBySessionIdOrderByCreatedAtAsc(Long sessionId, Pageable pageable);
}
