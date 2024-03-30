package com.securemicroservices.converter;

import com.securemicroservices.dto.PaymentDTO;
import com.securemicroservices.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {

    public PaymentDTO mapPaymentToDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .build();
    }

    public Payment mapDTOToPayment(PaymentDTO paymentDTO) {
        return Payment.builder()
                .id(paymentDTO.getId())
                .orderId(paymentDTO.getOrderId())
                .paymentDate(paymentDTO.getPaymentDate())
                .amount(paymentDTO.getAmount())
                .paymentMethod(paymentDTO.getPaymentMethod())
                .status(paymentDTO.getStatus())
                .build();
    }
}
