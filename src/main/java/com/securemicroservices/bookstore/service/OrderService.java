package com.securemicroservices.bookstore.service;

import com.securemicroservices.bookstore.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getAllOrders();

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO updateOrder(Long orderId, OrderDTO orderDTO);

    void deleteOrder(Long orderId);
}
