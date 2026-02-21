package com.assignment.chatstorage.controller;

import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.SessionRequestDto;
import com.assignment.chatstorage.dto.SessionResponseDto;
import com.assignment.chatstorage.dto.UpdateSessionRequestDto;
import com.assignment.chatstorage.service.ChatSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class ChatSessionController {
    private final ChatSessionService chatSessionService;

    @PostMapping
    public ApiResponse<SessionResponseDto> createChatSession(@Valid @RequestBody SessionRequestDto createSessionRequestDto){
        return chatSessionService.createSession(createSessionRequestDto);
    }

    @GetMapping("/{id}")
    public ApiResponse<SessionResponseDto> getSessionById(@PathVariable Long id){
        return chatSessionService.getSessionById(id);
    }

    @GetMapping("/users/{userId}")
    public ApiResponse<List<SessionResponseDto>> getSessionByUserId(@PathVariable String userId){
        return chatSessionService.getSessionByUserId(userId);
    }

    @PatchMapping("/{id}")
    public ApiResponse<SessionResponseDto> updateSessionById(@PathVariable Long id, @Valid @RequestBody UpdateSessionRequestDto updateSessionRequestDto){
        return chatSessionService.updateSession(id, updateSessionRequestDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSessionById(@PathVariable Long id){
        return chatSessionService.deleteSessionById(id);
    }
}
