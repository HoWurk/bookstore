package com.securemicroservices.bookstore.dto.converter;

import com.securemicroservices.bookstore.dto.OrderDTO;
import com.securemicroservices.bookstore.entity.Order;
import com.securemicroservices.bookstore.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class OrderConverter {
    private UserRepository userRepository;

    public OrderDTO mapOrderToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .build();
    }

    public Order mapDTOToOrder(OrderDTO orderDTO) {
        return Order.builder()
                .orderDate(orderDTO.getOrderDate())
                .user(userRepository.findById(orderDTO.getUserId())
                        .orElseThrow(() -> new NoSuchElementException("User not found with id: " + orderDTO.getUserId())))
                .build();
    }
}
