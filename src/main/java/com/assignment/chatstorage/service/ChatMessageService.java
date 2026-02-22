package com.assignment.chatstorage.service;

import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.MessageRequestDto;
import com.assignment.chatstorage.dto.MessageResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatMessageService {
    ApiResponse<MessageResponseDto> createMessage(Long sessionId, MessageRequestDto request);

    ApiResponse<List<MessageResponseDto>> getMessagesBySessionId(Long sessionId, int page, int size);

    ApiResponse<Void> deleteMessage(Long messageId);
}
