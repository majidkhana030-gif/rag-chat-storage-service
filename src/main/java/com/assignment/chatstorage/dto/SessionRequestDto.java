package com.assignment.chatstorage.dto;

import com.assignment.chatstorage.constant.AppConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public record SessionRequestDto(
        @NotBlank(message = AppConstants.USER_ID_REQUIRED)
        String userId,
        String title
) {}
