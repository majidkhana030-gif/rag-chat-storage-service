package com.assignment.chatstorage.controller;

import com.assignment.chatstorage.constant.SwaggerDocConstants;
import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.SessionRequestDto;
import com.assignment.chatstorage.dto.SessionResponseDto;
import com.assignment.chatstorage.dto.UpdateSessionRequestDto;
import com.assignment.chatstorage.service.ChatSessionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class ChatSessionController {
    private final ChatSessionService chatSessionService;

    @Operation(summary = SwaggerDocConstants.CREATE_CHAT_SESSION_SUMMARY, description = SwaggerDocConstants.CREATE_CHAT_SESSION_DESCRIPTION)
    @PostMapping
    public ApiResponse<SessionResponseDto> createChatSession(@Valid @RequestBody SessionRequestDto createSessionRequestDto){
        return chatSessionService.createSession(createSessionRequestDto);
    }

    @Operation(summary = SwaggerDocConstants.GET_SESSION_BY_ID_SUMMARY, description = SwaggerDocConstants.GET_SESSION_BY_ID_DESCRIPTION)
    @GetMapping("/{id}")
    public ApiResponse<SessionResponseDto> getSessionById(@PathVariable Long id){
        return chatSessionService.getSessionById(id);
    }

    @Operation(summary = SwaggerDocConstants.GET_SESSION_BY_USER_SUMMARY, description = SwaggerDocConstants.GET_SESSION_BY_USER_DESCRIPTION)
    @GetMapping("/users/{userId}")
    public ApiResponse<List<SessionResponseDto>> getSessionByUserId(@PathVariable String userId){
        return chatSessionService.getSessionByUserId(userId);
    }

    @Operation(summary = SwaggerDocConstants.UPDATE_SESSION_SUMMARY, description = SwaggerDocConstants.UPDATE_SESSION_DESCRIPTION)
    @PatchMapping("/{id}")
    public ApiResponse<SessionResponseDto> updateSessionById(@PathVariable Long id, @Valid @RequestBody UpdateSessionRequestDto updateSessionRequestDto){
        return chatSessionService.updateSession(id, updateSessionRequestDto);
    }

    @Operation(
            summary = SwaggerDocConstants.DELETE_SESSION_SUMMARY,
            description = SwaggerDocConstants.DELETE_SESSION_DESCRIPTION
    )
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSessionById(@PathVariable Long id){
        return chatSessionService.deleteSessionById(id);
    }
}
