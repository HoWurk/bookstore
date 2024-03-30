package com.securemicroservices.dto.converter;

import com.securemicroservices.dto.OrderItemDTO;
import com.securemicroservices.entity.OrderItem;
import com.securemicroservices.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class OrderItemConverter {
    private OrderRepository orderRepository;

    public OrderItemDTO mapOrderItemToDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .bookId(orderItem.getBookId())
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItem mapDTOToOrderItem(OrderItemDTO orderItemDTO) {
        return OrderItem.builder()
                .id(orderItemDTO.getId())
                .order(orderRepository.findById(orderItemDTO.getOrderId())
                        .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderItemDTO.getOrderId())))
                .bookId(orderItemDTO.getBookId())
                .quantity(orderItemDTO.getQuantity())
                .build();
    }
}
