package com.securemicroservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;

    @NotNull(message = "Order ID must not be null")
    private Long orderId;

    @NotNull(message = "Payment date must not be null")
    private LocalDateTime paymentDate;

    @NotNull(message = "Amount must not be null")
    private double amount;

    @NotEmpty(message = "Payment method must not be empty")
    private String paymentMethod;

    @NotEmpty(message = "Status must not be empty")
    private String status;
}

