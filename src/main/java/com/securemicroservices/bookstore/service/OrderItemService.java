package com.securemicroservices.bookstore.service;

import com.securemicroservices.bookstore.dto.OrderItemDTO;

import java.util.List;

public interface OrderItemService {

    OrderItemDTO getOrderItemById(Long orderItemId);

    List<OrderItemDTO> getAllOrderItems();

    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);

    OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO);

    void deleteOrderItem(Long orderItemId);
}

