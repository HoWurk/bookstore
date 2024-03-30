package com.securemicroservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long id;

    @NotNull(message = "Order ID must not be null")
    private Long orderId;

    @NotNull(message = "Book ID must not be null")
    private Long bookId;

    @NotNull(message = "Quantity must not be null")
    private int quantity;
}

