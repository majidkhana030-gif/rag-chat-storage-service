package com.assignment.chatstorage.repository;

import com.assignment.chatstorage.entity.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, Long> {
    List<ChatSessionEntity> findByUserIdAndStatus(String userId, String status);
    Optional<ChatSessionEntity> findByIdAndStatus(Long id, String status);


}
