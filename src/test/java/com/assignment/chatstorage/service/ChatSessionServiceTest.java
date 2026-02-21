package com.assignment.chatstorage.service;

import com.assignment.chatstorage.constant.AppConstants;
import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.SessionRequestDto;
import com.assignment.chatstorage.dto.SessionResponseDto;
import com.assignment.chatstorage.dto.UpdateSessionRequestDto;
import com.assignment.chatstorage.entity.ChatSessionEntity;
import com.assignment.chatstorage.exception.SessionNotFoundException;
import com.assignment.chatstorage.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatSessionServiceTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @InjectMocks
    private ChatSessionServiceImpl chatSessionService;

    @Test
    void createSession_shouldReturnSuccess() {

        SessionRequestDto requestDto = new SessionRequestDto("user1", "Test Session");

        ChatSessionEntity sessionEntity = new ChatSessionEntity();
        sessionEntity.setId(1L);
        sessionEntity.setUserId("user1");
        sessionEntity.setTitle("Test Session");

        when(chatSessionRepository.save(any(ChatSessionEntity.class))).thenReturn(sessionEntity);
        ApiResponse<SessionResponseDto> response = chatSessionService.createSession(requestDto);

        assertEquals(200, response.statusCode());
        assertEquals("user1", response.data().userId());
        assertEquals("Test Session", response.data().title());
    }

    @Test
    void getSession_shouldReturnSession() {

        ChatSessionEntity session = new ChatSessionEntity();
        session.setId(1L);
        session.setUserId("user1");
        session.setTitle("Test Session");
        session.setIsFavorite(false);

        when(chatSessionRepository.findByIdAndStatus(1L, "ACTIVE")).thenReturn(Optional.of(session));

        ApiResponse<SessionResponseDto> response = chatSessionService.getSessionById(1L);

        assertEquals(200, response.statusCode());
        assertEquals(1L, response.data().id());
        assertEquals("user1", response.data().userId());
        assertEquals("Test Session", response.data().title());
        assertEquals(false, response.data().isFavorite());
    }

    @Test
    void getSession_shouldThrowException_whenNotFound() {

        when(chatSessionRepository.findByIdAndStatus(1L, "ACTIVE")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> chatSessionService.getSessionById(1L));
    }

    @Test
    void getSessionByUserId_shouldReturnSessions() {
        ChatSessionEntity session = new ChatSessionEntity();
        session.setId(1L);
        session.setUserId("user1");
        session.setTitle("Test Session");

        when(chatSessionRepository.findByUserIdAndStatus("user1", AppConstants.SESSION_ACTIVE)).thenReturn(List.of(session));

        ApiResponse<List<SessionResponseDto>> response = chatSessionService.getSessionByUserId("user1");

        assertEquals(200, response.statusCode());
        assertEquals(1, response.data().size());
        assertEquals("Test Session", response.data().getFirst().title());
    }

    @Test
    void getSessionByUserId_shouldReturnEmptyList_whenNoSessions() {
        when(chatSessionRepository.findByUserIdAndStatus("user1", AppConstants.SESSION_ACTIVE)).thenReturn(List.of());

        ApiResponse<List<SessionResponseDto>> response = chatSessionService.getSessionByUserId("user1");

        assertEquals(200, response.statusCode());
        assertTrue(response.data().isEmpty());
    }

    @Test
    void updateSession_shouldUpdateTitleAndFavorite() {
        ChatSessionEntity session = new ChatSessionEntity();
        session.setId(1L);
        session.setUserId("user1");
        session.setTitle("Test Session");
        session.setIsFavorite(false);

        UpdateSessionRequestDto updateRequestDto = new UpdateSessionRequestDto("Updated Title", true);

        when(chatSessionRepository.findByIdAndStatus(1L, AppConstants.SESSION_ACTIVE))
                .thenReturn(Optional.of(session));
        when(chatSessionRepository.save(session)).thenReturn(session);

        ApiResponse<SessionResponseDto> response = chatSessionService.updateSession(1L, updateRequestDto);

        assertEquals(200, response.statusCode());
        assertEquals("Updated Title", response.data().title());
        assertTrue(response.data().isFavorite());
    }

    @Test
    void updateSession_shouldThrowException_whenNotFound() {
        UpdateSessionRequestDto updateRequestDto = new UpdateSessionRequestDto("Updated Title", true);

        when(chatSessionRepository.findByIdAndStatus(1L, AppConstants.SESSION_ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class,
                () -> chatSessionService.updateSession(1L, updateRequestDto));
    }

    @Test
    void deleteSessionById_shouldDeleteSession() {
        ChatSessionEntity session = new ChatSessionEntity();
        session.setId(1L);

        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(session));

        ApiResponse<Void> response = chatSessionService.deleteSessionById(1L);

        verify(chatSessionRepository, times(1)).delete(session);
        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteSessionById_shouldThrowException_whenNotFound() {
        when(chatSessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, () -> chatSessionService.deleteSessionById(1L));
    }
}
