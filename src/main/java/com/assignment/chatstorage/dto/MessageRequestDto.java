package com.assignment.chatstorage.dto;

import com.assignment.chatstorage.constant.AppConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

public record MessageRequestDto(
        @NotBlank(message = AppConstants.SENDER_TYPE_REQUIRED)
        String senderType,

        @NotBlank(message = AppConstants.CONTENT_CANT_EMPTY)
        String content,

        Map<String, Object> ragContext
) {}
