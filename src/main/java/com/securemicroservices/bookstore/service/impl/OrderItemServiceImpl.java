package com.securemicroservices.bookstore.service.impl;

import com.securemicroservices.bookstore.dto.OrderItemDTO;
import com.securemicroservices.bookstore.dto.converter.OrderItemConverter;
import com.securemicroservices.bookstore.entity.OrderItem;
import com.securemicroservices.bookstore.repository.BookRepository;
import com.securemicroservices.bookstore.repository.OrderItemRepository;
import com.securemicroservices.bookstore.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;
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
        OrderItem orderItem = orderItemConverter.mapDTOToOrderItem(orderItemDTO);
        orderItem = orderItemRepository.save(orderItem);
        return orderItemConverter.mapOrderItemToDTO(orderItem);
    }

    @Override
    public OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO) {
        OrderItem existingOrderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NoSuchElementException("OrderItem not found with id: " + orderItemId));
        existingOrderItem.setBook(bookRepository.findById(orderItemDTO.getBookId())
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + orderItemDTO.getBookId())));
        existingOrderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItemConverter.mapOrderItemToDTO(orderItemRepository.save(existingOrderItem));
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
}

