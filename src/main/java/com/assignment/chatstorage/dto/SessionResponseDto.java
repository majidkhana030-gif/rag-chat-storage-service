package com.assignment.chatstorage.dto;

import com.assignment.chatstorage.entity.ChatSessionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
public record SessionResponseDto(
        Long id,
        String userId,
        String title,
        Boolean isFavorite,
        String status,
        LocalDateTime createdAt
) {
    public static SessionResponseDto fromEntity(ChatSessionEntity entity){
        return new SessionResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getIsFavorite(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}