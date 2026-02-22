package com.assignment.chatstorage.controller;
import com.assignment.chatstorage.constant.AppConstants;
import com.assignment.chatstorage.dto.*;
import com.assignment.chatstorage.service.ChatSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatSessionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ChatSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatSessionService chatSessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createChatSession_shouldReturnSuccess() throws Exception {
        SessionRequestDto requestDto = new SessionRequestDto("user1", "Test Session");
        SessionResponseDto responseDto = new SessionResponseDto(1L, "user1", "Test Session", false,
                "ACTIVE", LocalDateTime.now());
        ApiResponse<SessionResponseDto> apiResponse = ApiResponse.success(responseDto, AppConstants.SESSION_CREATED);

        Mockito.when(chatSessionService.createSession(any(SessionRequestDto.class))).thenReturn(apiResponse);

        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value("user1"))
                .andExpect(jsonPath("$.data.title").value("Test Session"))
                .andExpect(jsonPath("$.message").value(AppConstants.SESSION_CREATED));
    }

    @Test
    void createChatSession_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        SessionRequestDto requestDto = new SessionRequestDto("", ""); // invalid input

        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSessionById_shouldReturnSuccess() throws Exception {
        SessionResponseDto responseDto = new SessionResponseDto(1L, "user1", "Test Session",
                false, "ACTIVE", LocalDateTime.now());
        ApiResponse<SessionResponseDto> apiResponse = ApiResponse.success(responseDto, AppConstants.SESSION_FETCHED);

        Mockito.when(chatSessionService.getSessionById(1L)).thenReturn(apiResponse);

        mockMvc.perform(get("/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.userId").value("user1"))
                .andExpect(jsonPath("$.message").value(AppConstants.SESSION_FETCHED));
    }

    @Test
    void getSessionById_shouldReturnNotFound_whenSessionMissing() throws Exception {
        Mockito.when(chatSessionService.getSessionById(99L))
                .thenThrow(new RuntimeException(AppConstants.SESSION_NOT_FOUND));

        mockMvc.perform(get("/sessions/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getSessionByUserId_shouldReturnSessions() throws Exception {
        SessionResponseDto dto = new SessionResponseDto(1L, "user1",
                "Test Session", false, "ACTIVE", LocalDateTime.now());
        ApiResponse<List<SessionResponseDto>> apiResponse = ApiResponse.success(List.of(dto), AppConstants.SESSION_FETCHED);

        Mockito.when(chatSessionService.getSessionByUserId("user1")).thenReturn(apiResponse);

        mockMvc.perform(get("/sessions/users/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].userId").value("user1"))
                .andExpect(jsonPath("$.data[0].title").value("Test Session"))
                .andExpect(jsonPath("$.message").value(AppConstants.SESSION_FETCHED));
    }

    @Test
    void getSessionByUserId_shouldReturnEmptyList() throws Exception {
        ApiResponse<List<SessionResponseDto>> apiResponse = ApiResponse.success(List.of(), AppConstants.NO_SESSION_FOUND);

        Mockito.when(chatSessionService.getSessionByUserId("user1")).thenReturn(apiResponse);

        mockMvc.perform(get("/sessions/users/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(AppConstants.NO_SESSION_FOUND));
    }

    @Test
    void updateSessionById_shouldReturnSuccess() throws Exception {
        UpdateSessionRequestDto updateDto = new UpdateSessionRequestDto("Updated Title", true);
        SessionResponseDto responseDto = new SessionResponseDto(1L, "user1",
                "Updated Title", true, "ACTIVE", LocalDateTime.now());
        ApiResponse<SessionResponseDto> apiResponse = ApiResponse.success(responseDto, AppConstants.SESSION_UPDATED);

        Mockito.when(chatSessionService.updateSession(eq(1L), any(UpdateSessionRequestDto.class))).thenReturn(apiResponse);

        mockMvc.perform(patch("/sessions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Title"))
                .andExpect(jsonPath("$.data.isFavorite").value(true))
                .andExpect(jsonPath("$.message").value(AppConstants.SESSION_UPDATED));
    }

    @Test
    void updateSessionById_shouldReturnNotFound_whenSessionMissing() throws Exception {
        UpdateSessionRequestDto updateDto = new UpdateSessionRequestDto("Updated Title", true);

        Mockito.when(chatSessionService.updateSession(eq(99L), any(UpdateSessionRequestDto.class)))
                .thenThrow(new RuntimeException(AppConstants.SESSION_NOT_FOUND));

        mockMvc.perform(patch("/sessions/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteSessionById_shouldReturnSuccess() throws Exception {
        ApiResponse<Void> apiResponse = ApiResponse.success(AppConstants.SESSION_DELETED_SUCCESSFULLY);

        Mockito.when(chatSessionService.deleteSessionById(1L)).thenReturn(apiResponse);

        mockMvc.perform(delete("/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AppConstants.SESSION_DELETED_SUCCESSFULLY));
    }

    @Test
    void deleteSessionById_shouldReturnNotFound_whenSessionMissing() throws Exception {
        Mockito.when(chatSessionService.deleteSessionById(99L))
                .thenThrow(new RuntimeException(AppConstants.SESSION_NOT_FOUND));

        mockMvc.perform(delete("/sessions/99"))
                .andExpect(status().isInternalServerError());
    }
}
