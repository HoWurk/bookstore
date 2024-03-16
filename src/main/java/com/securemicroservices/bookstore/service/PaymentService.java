package com.securemicroservices.bookstore.service;

import com.securemicroservices.bookstore.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO getPaymentById(Long paymentId);

    List<PaymentDTO> getAllPayments();

    PaymentDTO createPayment(PaymentDTO paymentDTO);

    PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO);

    void deletePayment(Long paymentId);
}

