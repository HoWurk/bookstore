package com.securemicroservices.service;

import com.securemicroservices.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getAllOrders();

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO updateOrder(Long orderId, OrderDTO orderDTO);

    void deleteOrder(Long orderId);

    boolean validateOrder(Long orderId);

    boolean holdItemsInOrder(Long orderId);

    boolean releaseItemsInOrder(Long orderId);

    boolean completeTransaction(Long orderId);
}
