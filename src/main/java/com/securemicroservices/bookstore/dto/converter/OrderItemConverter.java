package com.securemicroservices.bookstore.dto.converter;

import com.securemicroservices.bookstore.dto.OrderItemDTO;
import com.securemicroservices.bookstore.entity.OrderItem;
import com.securemicroservices.bookstore.repository.BookRepository;
import com.securemicroservices.bookstore.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class OrderItemConverter {
    private OrderRepository orderRepository;
    private BookRepository bookRepository;

    public OrderItemDTO mapOrderItemToDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .bookId(orderItem.getBook().getId())
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItem mapDTOToOrderItem(OrderItemDTO orderItemDTO) {
        return OrderItem.builder()
                .id(orderItemDTO.getId())
                .order(orderRepository.findById(orderItemDTO.getOrderId())
                        .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderItemDTO.getOrderId())))
                .book(bookRepository.findById(orderItemDTO.getBookId())
                        .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + orderItemDTO.getBookId())))
                .quantity(orderItemDTO.getQuantity())
                .build();
    }
}
