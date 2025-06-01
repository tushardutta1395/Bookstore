package com.example.Bookstore.payload;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorDTO(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<String> validationErrors
        ) {
}
