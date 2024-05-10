package com.edig.searchengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Schema(name = "ErrorResponse", description = "Schema to represent an Error Response and hold the status code and message")
@Data @AllArgsConstructor
public class ErrorResponseDTO {
    @Schema(description = "API Path that caused the error", example = "/api/v1/products")
    private String apiPath;
    @Schema(description = "HTTP Status Code in the response", example = "404")
    private HttpStatus errorCode;
    @Schema(description = "Error Message in the response", example = "Product not found")
    private String errorMessage;
    @Schema(description = " Time of the error", example = "2021-09-01T12:00:00")
    private LocalDateTime errorTime;
}
