package com.edig.searchengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(name = "Product", description = "Schema to represent a Product")
public class ProductDTO {
    @Schema(description = "Product Name", example = "Laptop1")
    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Schema(description = "Brand Name", example = "Dell")
    @NotEmpty(message = "Brand is required")
    @Size(min = 3, max = 50, message = "Brand must be between 3 and 50 characters")
    private String brand;

    @Schema(description = "Made In", example = "USA")
    @NotEmpty(message = "Made In is required")
    @Size(min = 3, max = 50, message = "Made In must be between 3 and 50 characters")
    private String madeIn;

    @Schema(description = "Price", example = "1000.00")
    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Digits(integer = 6, fraction = 2, message = "Price must be a number with up to 6 digits integer and 2 digits fraction")
    private Float price;

    @Schema(description = "Category Name", example = "Electronics")
    @NotEmpty(message = "Category Name is required")
    private String categoryName;
}
