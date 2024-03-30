package com.securemicroservices.service;

import com.securemicroservices.dto.OrderItemDTO;

import java.util.List;

public interface OrderItemService {

    OrderItemDTO getOrderItemById(Long orderItemId);

    List<OrderItemDTO> getAllOrderItems();

    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);

    OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO);

    void deleteOrderItem(Long orderItemId);

    List<OrderItemDTO> getAllOrderItemsByOrderId(Long orderId);
}

