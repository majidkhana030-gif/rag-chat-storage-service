package com.assignment.chatstorage.service;

import com.assignment.chatstorage.constant.AppConstants;
import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.SessionRequestDto;
import com.assignment.chatstorage.dto.SessionResponseDto;
import com.assignment.chatstorage.dto.UpdateSessionRequestDto;
import com.assignment.chatstorage.entity.ChatSessionEntity;
import com.assignment.chatstorage.exception.SessionNotFoundException;
import com.assignment.chatstorage.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSessionServiceImpl implements ChatSessionService{

    private final ChatSessionRepository chatSessionRepository;

    @Override
    public ApiResponse<SessionResponseDto> createSession(SessionRequestDto request) {
        log.info("Creating chat session for userId={}, title={}", request.userId(), request.title());

        ChatSessionEntity chatSessionEntity = new ChatSessionEntity();

        chatSessionEntity.setUserId(request.userId());
        chatSessionEntity.setTitle(request.title() != null ? request.title() : AppConstants.DEFAULT_CHAT_TITLE);

        ChatSessionEntity savedSession = chatSessionRepository.save(chatSessionEntity);
        return ApiResponse.success(SessionResponseDto.fromEntity(savedSession), AppConstants.SESSION_CREATED);
    }

    @Override
    public ApiResponse<SessionResponseDto> getSessionById(Long id) {
        log.info("Get  session for Id={}", id);
        ChatSessionEntity sessionEntity = chatSessionRepository.findByIdAndStatus(id, AppConstants.SESSION_ACTIVE).orElseThrow(()->new SessionNotFoundException((AppConstants.SESSION_NOT_FOUND.formatted(id))));
        return ApiResponse.success(SessionResponseDto.fromEntity(sessionEntity), AppConstants.SESSION_FETCHED);
    }

    public ApiResponse<List<SessionResponseDto>> getSessionByUserId(String userId){
        log.info("Get  session for UserId={}", userId);
       List<ChatSessionEntity> userSessionRecords = chatSessionRepository.findByUserIdAndStatus(userId, AppConstants.SESSION_ACTIVE);
        if(userSessionRecords.isEmpty()){
            return ApiResponse.success(List.of(), AppConstants.NO_SESSION_FOUND);
        }
       return ApiResponse.success(userSessionRecords.stream().map(SessionResponseDto::fromEntity).toList(),AppConstants.SESSION_FETCHED);
    }

    @Override
    public ApiResponse<SessionResponseDto> updateSession(Long id, UpdateSessionRequestDto requestDto) {
        ChatSessionEntity sessionEntity = chatSessionRepository.findByIdAndStatus(id, AppConstants.SESSION_ACTIVE).orElseThrow(()->new SessionNotFoundException((AppConstants.SESSION_NOT_FOUND.formatted(id))));

        if(requestDto.title() != null){
            sessionEntity.setTitle(requestDto.title());
        }

        if(requestDto.isFavorite() != null){
            sessionEntity.setIsFavorite(requestDto.isFavorite());
        }

        log.info("Update session for id={}", id);
        // Save updated session
        ChatSessionEntity updatedSession  = chatSessionRepository.save(sessionEntity);
        return ApiResponse.success(SessionResponseDto.fromEntity(updatedSession),AppConstants.SESSION_UPDATED);
    }

    @Override
    public ApiResponse<Void> deleteSessionById(Long id) {
        ChatSessionEntity sessionEntity = chatSessionRepository.findById(id).orElseThrow(()->new SessionNotFoundException((AppConstants.SESSION_NOT_FOUND)));
        chatSessionRepository.delete(sessionEntity);

        return ApiResponse.success(AppConstants.SESSION_DELETED_SUCCESSFULLY);
    }
}
