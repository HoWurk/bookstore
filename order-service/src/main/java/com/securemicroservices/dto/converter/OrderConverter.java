package com.securemicroservices.dto.converter;

import com.securemicroservices.dto.OrderDTO;
import com.securemicroservices.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderConverter {

    public OrderDTO mapOrderToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .build();
    }

    public Order mapDTOToOrder(OrderDTO orderDTO) {
        return Order.builder()
                .orderDate(orderDTO.getOrderDate())
                .userId(orderDTO.getUserId())
                .build();
    }
}
