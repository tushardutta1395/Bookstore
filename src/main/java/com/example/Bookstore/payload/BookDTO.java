package com.example.Bookstore.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BookDTO(

        @NotBlank
        @Size(min = 10, max = 30)
        @Schema(description = "Book Title", example = "Harry Potter", requiredMode = RequiredMode.REQUIRED)
        String title,

        @NotBlank
        @Size(min = 5, max = 15)
        @Schema(description = "Author Name", example = "J.K. Rowling", requiredMode = RequiredMode.REQUIRED)
        String author,

        @Schema(description = "Book Price", example = "599.99", requiredMode = RequiredMode.REQUIRED)
        BigDecimal price
) {
}
