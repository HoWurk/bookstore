package com.securemicroservices.bookstore.service.impl;

import com.securemicroservices.bookstore.dto.OrderDTO;
import com.securemicroservices.bookstore.dto.converter.OrderConverter;
import com.securemicroservices.bookstore.entity.Order;
import com.securemicroservices.bookstore.repository.OrderRepository;
import com.securemicroservices.bookstore.repository.UserRepository;
import com.securemicroservices.bookstore.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderConverter orderConverter;

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderId));
        return orderConverter.mapOrderToDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderConverter::mapOrderToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderConverter.mapDTOToOrder(orderDTO);
        order = orderRepository.save(order);
        return orderConverter.mapOrderToDTO(order);
    }

    @Override
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderId));
        existingOrder.setOrderDate(orderDTO.getOrderDate());
        return orderConverter.mapOrderToDTO(orderRepository.save(existingOrder));
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}

