package com.securemicroservices.service.impl;

import com.securemicroservices.dto.OrderItemDTO;
import com.securemicroservices.dto.converter.OrderItemConverter;
import com.securemicroservices.entity.OrderItem;
import com.securemicroservices.repository.OrderItemRepository;
import com.securemicroservices.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private RestTemplate restTemplate;

    private final OrderItemRepository orderItemRepository;
    private final OrderItemConverter orderItemConverter;

    @Override
    public OrderItemDTO getOrderItemById(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NoSuchElementException("OrderItem not found with id: " + orderItemId));
        return orderItemConverter.mapOrderItemToDTO(orderItem);
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream()
                .map(orderItemConverter::mapOrderItemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
        validateBookQuantity(orderItemDTO.getBookId(), orderItemDTO.getQuantity());

        OrderItem orderItem = orderItemConverter.mapDTOToOrderItem(orderItemDTO);
        orderItem = orderItemRepository.save(orderItem);
        return orderItemConverter.mapOrderItemToDTO(orderItem);
    }

    @Override
    public OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO) {
        OrderItem existingOrderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NoSuchElementException("OrderItem not found with id: " + orderItemId));
        validateBookQuantity(orderItemDTO.getBookId(), orderItemDTO.getQuantity());

        existingOrderItem.setBookId(orderItemDTO.getBookId());
        existingOrderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItemConverter.mapOrderItemToDTO(orderItemRepository.save(existingOrderItem));
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }

    /*private BookDTO validateBookExistence(Long bookId) {
        String warehouseServiceUrl = "http://app:8080";
        try {
            return restTemplate.getForObject(warehouseServiceUrl + "/books/{bookId}", BookDTO.class, bookId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NoSuchElementException("Book from OrderItem not found with id: " + bookId);
        }
    }*/

    private void validateBookQuantity(Long bookId, int requestedQuantity) {
        String warehouseServiceUrl = "http://app:8080";

        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(warehouseServiceUrl + "/books/{bookId}/availability?quantity={quantity}",
                    Boolean.class, bookId, requestedQuantity);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new IllegalStateException("Failed to check book availability: " + response.getStatusCode());
            }

            if (Boolean.FALSE.equals(response.getBody())) {
                throw new IllegalStateException("Not enough quantity available for book with ID: " + bookId);
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new NoSuchElementException("Book not found with ID: " + bookId);
        }
    }

    public List<OrderItemDTO> getAllOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.getAllOrderItemsByOrderId(orderId).stream()
                .map(orderItemConverter::mapOrderItemToDTO)
                .collect(Collectors.toList());
    }

}

