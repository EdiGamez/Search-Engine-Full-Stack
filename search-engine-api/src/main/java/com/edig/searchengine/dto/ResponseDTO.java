package com.edig.searchengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
@Schema(name = "Response", description = "Schema to represent a Full Response and hold the status code and message")
@Data @AllArgsConstructor
public class ResponseDTO {
    @Schema(description = "Status Code in the response", example = "200")
    private String statusCode;
    @Schema(description = "Status Message in the response", example = "Success")
    private String statusMessage;
}
