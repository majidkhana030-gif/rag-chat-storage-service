package com.assignment.chatstorage.service;

import com.assignment.chatstorage.constant.AppConstants;
import com.assignment.chatstorage.dto.*;
import com.assignment.chatstorage.entity.ChatMessageEntity;
import com.assignment.chatstorage.entity.ChatSessionEntity;
import com.assignment.chatstorage.exception.MessageNotFoundException;
import com.assignment.chatstorage.exception.SessionNotFoundException;
import com.assignment.chatstorage.repository.ChatMessageRepository;
import com.assignment.chatstorage.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatMessageRepository messageRepository;
    private final ChatSessionRepository sessionRepository;

    @Override
    public ApiResponse<MessageResponseDto> createMessage(Long sessionId, MessageRequestDto request) {
        log.info("Creating chat message for sessionId={}, senderType={}", sessionId, request.senderType());

        ChatSessionEntity session = sessionRepository.findByIdAndStatus(sessionId, AppConstants.SESSION_ACTIVE)
                .orElseThrow(() -> new SessionNotFoundException(AppConstants.SESSION_NOT_FOUND.formatted(sessionId)));

        ChatMessageEntity saveMessageEntity = new ChatMessageEntity();
        saveMessageEntity.setSession(session);
        saveMessageEntity.setSenderType(request.senderType());
        saveMessageEntity.setContent(request.content());
        saveMessageEntity.setRagContext(request.ragContext());

        ChatMessageEntity savedMessageResponse = messageRepository.save(saveMessageEntity);
        return ApiResponse.success(MessageResponseDto.fromEntity(savedMessageResponse),AppConstants.MESSAGE_CREATED);
    }

    @Override
    public ApiResponse<Page<MessageResponseDto>> getMessagesBySessionId(
            Long sessionId, int page, int size){
        log.info("Get chat message for sessionId={}", sessionId);

        sessionRepository.findByIdAndStatus(sessionId, AppConstants.SESSION_ACTIVE)
                .orElseThrow(() -> new SessionNotFoundException(AppConstants.SESSION_NOT_FOUND.formatted(sessionId)));

        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponseDto> messages = messageRepository
                .findBySessionIdOrderByCreatedAtAsc(sessionId, pageable)
                .map(MessageResponseDto::fromEntity);

        return ApiResponse.success(messages, AppConstants.MESSAGE_FETCHED);
    }

    @Override
    public ApiResponse<Void> deleteMessage(Long messageId) {
        log.info("Delete chat message for messageId={}", messageId);
        if(!messageRepository.existsById(messageId)){
            throw new MessageNotFoundException(AppConstants.MESSAGE_NOT_FOUND);
        }
        messageRepository.deleteById(messageId);
        return ApiResponse.success(AppConstants.MESSAGE_DELETED);
    }
}
