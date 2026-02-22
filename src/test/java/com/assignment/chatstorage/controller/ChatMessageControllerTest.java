package com.assignment.chatstorage.controller;

import com.assignment.chatstorage.constant.AppConstants;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.MessageRequestDto;
import com.assignment.chatstorage.dto.MessageResponseDto;
import com.assignment.chatstorage.service.ChatMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ChatMessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createMessage_shouldReturnSuccess() throws Exception {

        MessageResponseDto responseDto = new MessageResponseDto(1L, "User", "Hello", null, null);

        ApiResponse<MessageResponseDto> apiResponse = ApiResponse.success(responseDto, AppConstants.MESSAGE_CREATED);

        Mockito.when(chatMessageService.createMessage(eq(1L), any())).thenReturn(apiResponse);

        MessageRequestDto requestDto = new MessageRequestDto("User", "Hello", null);

        mockMvc.perform(post("/sessions/1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.content").value("Hello"));
    }

    @Test
    void createMessage_shouldReturnBadRequest_whenInvalidInput() throws Exception {

        MessageRequestDto requestDto = new MessageRequestDto("", "", null);

        mockMvc.perform(post("/sessions/1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessages_shouldReturnPaginatedResponse() throws Exception {

        MessageResponseDto dto = new MessageResponseDto(1L, "User", "Hello", null, null);

        List<MessageResponseDto> messages = List.of(dto);

        ApiResponse<List<MessageResponseDto>> apiResponse =
                ApiResponse.success(messages, AppConstants.MESSAGE_FETCHED);

        Mockito.when(chatMessageService.getMessagesBySessionId(1L, 0, 20))
                .thenReturn(apiResponse);

        mockMvc.perform(get("/sessions/1/messages")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].content").value("Hello"));
    }

    @Test
    void deleteMessage_shouldReturnSuccess() throws Exception {

        ApiResponse<Void> apiResponse = ApiResponse.success("Message deleted successfully");

        Mockito.when(chatMessageService.deleteMessage(1L)).thenReturn(apiResponse);

        mockMvc.perform(delete("/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Message deleted successfully"));
    }

    @Test
    void deleteMessage_shouldReturnNotFound_whenMessageMissing() throws Exception {

        Mockito.when(chatMessageService.deleteMessage(99L)).thenThrow(new RuntimeException("Message not found"));

        mockMvc.perform(delete("/messages/99")).andExpect(status().isInternalServerError());
    }
}
