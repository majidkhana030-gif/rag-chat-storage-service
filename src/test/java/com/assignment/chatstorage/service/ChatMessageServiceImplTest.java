package com.assignment.chatstorage.service;

import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.MessageRequestDto;
import com.assignment.chatstorage.dto.MessageResponseDto;
import com.assignment.chatstorage.entity.ChatMessageEntity;
import com.assignment.chatstorage.entity.ChatSessionEntity;
import com.assignment.chatstorage.exception.MessageNotFoundException;
import com.assignment.chatstorage.exception.SessionNotFoundException;
import com.assignment.chatstorage.repository.ChatMessageRepository;
import com.assignment.chatstorage.repository.ChatSessionRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceImplTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @Test
    void shouldCreateMessageSuccessfully(){
        Long sessionId = 1L;

        ChatSessionEntity session = new ChatSessionEntity();
        session.setId(sessionId);

        MessageRequestDto request = new MessageRequestDto("User", "Hello", Map.of());

        ChatMessageEntity savedEntity = new ChatMessageEntity();
        savedEntity.setId(10L);
        savedEntity.setSession(session);
        savedEntity.setSenderType("User");
        savedEntity.setContent("Hello");

        when(chatSessionRepository.findByIdAndStatus(eq(sessionId), any())).thenReturn(Optional.of(session));
        when(chatMessageRepository.save(any(ChatMessageEntity.class))).thenReturn(savedEntity);
        ApiResponse<MessageResponseDto> response = chatMessageService.createMessage(sessionId, request);

        assertEquals(200, response.statusCode());
        assertNotNull(response.data());
        assertEquals("Hello", response.data().content());

        verify(chatMessageRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFoundWhileCreatingMessage(){
        when(chatSessionRepository.findByIdAndStatus(anyLong(), any())).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, () ->
                chatMessageService.createMessage(1L,
                        new MessageRequestDto("USER", "Hi", null)));
    }

    @Test
    void shouldReturnPagedMessagesSuccessfully(){
        Long sessionId = 1L;

        ChatSessionEntity session = new ChatSessionEntity();
        session.setId(sessionId);

        when(chatSessionRepository.findByIdAndStatus(eq(sessionId), any())).thenReturn(Optional.of(session));

        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setId(1L);
        entity.setContent("Test Message");

        Page<ChatMessageEntity> page =
                new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);

        when(chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(eq(sessionId), any(Pageable.class)))
                .thenReturn(page);

        ApiResponse<Page<MessageResponseDto>> response =
                chatMessageService.getMessagesBySessionId(sessionId, 0, 10);

        assertEquals(200, response.statusCode());
        assertEquals(1, response.data().getContent().size());
        assertEquals("Test Message",
                response.data().getContent().getFirst().content());
    }

    @Test
    void shouldReturnEmptyPageWhenNoMessagesFound(){
        Long sessionId = 1L;

        ChatSessionEntity session = new ChatSessionEntity();
        session.setId(sessionId);

        when(chatSessionRepository.findByIdAndStatus(eq(sessionId), any())).thenReturn(Optional.of(session));

        Page<ChatMessageEntity> emptyPage =
                new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        when(chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(eq(sessionId), any(Pageable.class)))
                .thenReturn(emptyPage);

        ApiResponse<Page<MessageResponseDto>> response =
                chatMessageService.getMessagesBySessionId(sessionId, 0, 10);

        assertEquals(200, response.statusCode());
        assertTrue(response.data().getContent().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFoundWhileFetchingMessages(){
        when(chatSessionRepository.findByIdAndStatus(anyLong(), any())).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, () ->
                chatMessageService.getMessagesBySessionId(1L, 0, 10));
    }

    @Test
    void shouldDeleteMessageSuccessfully(){
        when(chatMessageRepository.existsById(1L)).thenReturn(true);

        ApiResponse<Void> response = chatMessageService.deleteMessage(1L);

        assertEquals(200, response.statusCode());
        assertNull(response.data());

        verify(chatMessageRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenMessageNotFound(){
        when(chatMessageRepository.existsById(1L)).thenReturn(false);

        assertThrows(MessageNotFoundException.class, () -> chatMessageService.deleteMessage(1L));
    }
}
