package com.securemicroservices.bookstore.service.impl;

import com.securemicroservices.bookstore.dto.PaymentDTO;
import com.securemicroservices.bookstore.dto.converter.PaymentConverter;
import com.securemicroservices.bookstore.entity.Payment;
import com.securemicroservices.bookstore.repository.OrderRepository;
import com.securemicroservices.bookstore.repository.PaymentRepository;
import com.securemicroservices.bookstore.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;
    private PaymentConverter paymentConverter;

    @Override
    public PaymentDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found with id: " + paymentId));
        return paymentConverter.mapPaymentToDTO(payment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(paymentConverter::mapPaymentToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = paymentConverter.mapDTOToPayment(paymentDTO);
        payment = paymentRepository.save(payment);
        return paymentConverter.mapPaymentToDTO(payment);
    }

    @Override
    public PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO) {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found with id: " + paymentId));
        existingPayment.setPaymentDate(paymentDTO.getPaymentDate());
        existingPayment.setAmount(paymentDTO.getAmount());
        existingPayment.setPaymentMethod(paymentDTO.getPaymentMethod());
        existingPayment.setStatus(paymentDTO.getStatus());
        return paymentConverter.mapPaymentToDTO(paymentRepository.save(existingPayment));
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
