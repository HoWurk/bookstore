package com.securemicroservices.service.impl;

import com.securemicroservices.converter.PaymentConverter;
import com.securemicroservices.dto.OrderDTO;
import com.securemicroservices.dto.PaymentDTO;
import com.securemicroservices.entity.Payment;
import com.securemicroservices.repository.PaymentRepository;
import com.securemicroservices.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    public static final String GATEWAY_SERVICE_URL = "http://gateway-service:8080";
    private RestTemplate restTemplate;

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
        validateOrderExistence(paymentDTO.getOrderId());

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

    private void validateOrderExistence(Long orderId) {
        try {
            OrderDTO order = restTemplate.getForObject(GATEWAY_SERVICE_URL + "/orders/{orderId}", OrderDTO.class, orderId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NoSuchElementException("Payment Order not found with id: " + orderId);
        }
    }

    @Override
    public PaymentDTO completePayment(PaymentDTO paymentDTO) {
        try {
            assurePaymentNotPaid(paymentDTO);

            holdItemsInPayment(paymentDTO);

            completePaymentTransaction(paymentDTO);

            PaymentDTO updatedPayment = updatePayment(paymentDTO.getId(), paymentDTO);
            /*
            If Payment fails (non-existent logic at this point in time)

            if (updatedPayment == null){
                releaseItemsInOrder(paymentDTO.getOrderId())
            }
             */
            return updatedPayment;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to complete payment: " + e.getMessage(), e);
        }
    }

    private void assurePaymentNotPaid(PaymentDTO paymentDTO) {
        Long paymentId = paymentDTO.getId();
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found with id: " + paymentId));
        if (payment.getStatus().equals("PAID"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment already paid. Cancelling payment process.");
    }

    private void holdItemsInPayment(PaymentDTO paymentDTO) {
        boolean itemsHeld = holdItemsInOrder(paymentDTO.getOrderId());
        if (!itemsHeld) {
            throw new IllegalStateException("Failed to hold items in the order. Payment cannot be completed.");
        }
    }

    private void completePaymentTransaction(PaymentDTO paymentDTO) {
        boolean success = completeTransaction(paymentDTO.getOrderId());
        if (!success) {
            releaseItemsInOrder(paymentDTO);
            throw new IllegalStateException("Failed to complete transaction. Payment cannot be completed.");
        }
    }

    private void releaseItemsInOrder(PaymentDTO paymentDTO) {
        boolean itemsReleased = releaseItemsInOrder(paymentDTO.getOrderId());
        if (!itemsReleased) {
            throw new IllegalStateException("Failed to complete transaction. Failed to release items. Critical error.");
        }
    }

    private boolean completeTransaction(Long orderId) {
        return getConfirmation(GATEWAY_SERVICE_URL + "/orders/{orderId}/complete", orderId);
    }

    private boolean holdItemsInOrder(Long orderId) {
        return getConfirmation(GATEWAY_SERVICE_URL + "/orders/{orderId}/hold", orderId);
    }

    private boolean releaseItemsInOrder(Long orderId) {
        return getConfirmation(GATEWAY_SERVICE_URL + "/orders/{orderId}/release", orderId);
    }

    /*private boolean validateOrder(Long orderId) {
        return getConfirmation(ORDER_SERVICE_URL + "/orders/{orderId}/validate", orderId);
    }*/

    private boolean getConfirmation(String url, Long insertedValue) {
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Boolean.class,
                    insertedValue
            );

            return Boolean.TRUE.equals(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
