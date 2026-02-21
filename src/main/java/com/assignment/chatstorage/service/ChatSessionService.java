package com.assignment.chatstorage.service;

import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.SessionRequestDto;
import com.assignment.chatstorage.dto.SessionResponseDto;
import com.assignment.chatstorage.dto.UpdateSessionRequestDto;
import com.assignment.chatstorage.entity.ChatSessionEntity;

import java.util.List;

public interface ChatSessionService {
    ApiResponse<SessionResponseDto> createSession(SessionRequestDto request);
    ApiResponse<SessionResponseDto> getSessionById(Long id);
    ApiResponse<List<SessionResponseDto>> getSessionByUserId(String userId);
    ApiResponse<SessionResponseDto> updateSession(Long id, UpdateSessionRequestDto requestDto);
    ApiResponse<Void> deleteSessionById(Long id);

}
