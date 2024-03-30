package com.securemicroservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "Author must not be empty")
    private String author;

    @NotEmpty(message = "Description must not be empty")
    private String description;

    @NotEmpty(message = "ISBN must not be empty")
    private String isbn;

    @NotNull(message = "Price must not be null")
    private double price;

    @NotNull(message = "Quantity available must not be null")
    private int quantityAvailable;
}


