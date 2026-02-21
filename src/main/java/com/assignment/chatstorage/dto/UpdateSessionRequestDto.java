package com.assignment.chatstorage.dto;

import com.assignment.chatstorage.constant.AppConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record UpdateSessionRequestDto(
        @NotBlank(message = AppConstants.TITLE_CAN_BLANK)
        String title,
        Boolean isFavorite
) {}