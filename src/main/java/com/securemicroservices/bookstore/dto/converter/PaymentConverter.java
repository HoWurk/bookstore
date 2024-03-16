package com.securemicroservices.bookstore.dto.converter;

import com.securemicroservices.bookstore.dto.PaymentDTO;
import com.securemicroservices.bookstore.entity.Payment;
import com.securemicroservices.bookstore.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class PaymentConverter {
    private OrderRepository orderRepository;

    public PaymentDTO mapPaymentToDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .build();
    }

    public Payment mapDTOToPayment(PaymentDTO paymentDTO) {
        return Payment.builder()
                .id(paymentDTO.getId())
                .order(orderRepository.findById(paymentDTO.getOrderId())
                        .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + paymentDTO.getOrderId())))
                .paymentDate(paymentDTO.getPaymentDate())
                .amount(paymentDTO.getAmount())
                .paymentMethod(paymentDTO.getPaymentMethod())
                .status(paymentDTO.getStatus())
                .build();
    }
}
