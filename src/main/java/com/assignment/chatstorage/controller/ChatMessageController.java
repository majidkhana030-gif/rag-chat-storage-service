package com.assignment.chatstorage.controller;

import com.assignment.chatstorage.dto.ApiResponse;
import com.assignment.chatstorage.dto.MessageRequestDto;
import com.assignment.chatstorage.dto.MessageResponseDto;
import com.assignment.chatstorage.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/sessions/{sessionId}/messages")
    public ApiResponse<MessageResponseDto> createMessage(@PathVariable   Long sessionId, @Valid @RequestBody MessageRequestDto requestDto){
        return chatMessageService.createMessage(sessionId, requestDto);
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public ApiResponse<Page<MessageResponseDto>> getMessageBySessionId(@PathVariable Long sessionId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20")  int size){
    return chatMessageService.getMessagesBySessionId(sessionId, page, size);
    }

    @DeleteMapping("/messages/{messageId}")
    public ApiResponse<Void> deleteMessage(@PathVariable Long messageId){
        return chatMessageService.deleteMessage(messageId);

    }
}
