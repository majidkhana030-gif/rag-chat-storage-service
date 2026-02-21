package com.assignment.chatstorage.dto;

import com.assignment.chatstorage.entity.ChatMessageEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

public record MessageResponseDto(
        Long id,
        String senderType,
        String content,
        Map<String, Object> ragContext,
        LocalDateTime createdAt
) {
    public static MessageResponseDto fromEntity(ChatMessageEntity entity){
        return new MessageResponseDto(
                entity.getId(),
                entity.getSenderType(),
                entity.getContent(),
                entity.getRagContext(),
                entity.getCreatedAt()
        );
    }
}