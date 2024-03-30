package com.securemicroservices.service.impl;

import com.securemicroservices.dto.OrderDTO;
import com.securemicroservices.dto.OrderItemDTO;
import com.securemicroservices.dto.converter.OrderConverter;
import com.securemicroservices.entity.Order;
import com.securemicroservices.repository.OrderRepository;
import com.securemicroservices.service.OrderItemService;
import com.securemicroservices.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    public static final String GATEWAY_SERVICE_URL = "http://gateway-service:8080";
    private RestTemplate restTemplate;

    private OrderRepository orderRepository;
    private OrderConverter orderConverter;
    private OrderItemService orderItemService;

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

    @Override
    public boolean validateOrder(Long orderId) {
        return performOrderAction(orderId, this::isQuantityAvailable);
    }

    @Override
    public boolean holdItemsInOrder(Long orderId) {
        return performOrderAction(orderId, this::holdItems);
    }

    @Override
    public boolean releaseItemsInOrder(Long orderId) {
        return performOrderAction(orderId, this::releaseItems);
    }

    @Override
    public boolean completeTransaction(Long orderId) {
        return performOrderAction(orderId, this::deductItems);
    }

    private boolean performOrderAction(Long orderId, Function<Map<Long, Integer>, Boolean> action) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderId));
        Map<Long, Integer> itemList = gatherItems(orderId);
        return action.apply(itemList);
    }

    private Map<Long, Integer> gatherItems(Long orderId) {
        List<OrderItemDTO> orderItems = orderItemService.getAllOrderItemsByOrderId(orderId);
        HashMap<Long, Integer> itemList = new HashMap<>();
        for (OrderItemDTO orderItem : orderItems) {
            Long bookId = orderItem.getBookId();
            int quantity = orderItem.getQuantity();
            if (itemList.containsKey(bookId)) {
                itemList.put(bookId, itemList.get(bookId) + quantity);
            } else {
                itemList.put(bookId, quantity);
            }
        }
        return itemList;
    }

    private boolean holdItems(Map<Long, Integer> orderItems) {
        return invokeWarehouseService("/books/hold", orderItems);
    }

    private boolean releaseItems(Map<Long, Integer> orderItems) {
        return invokeWarehouseService("/books/release", orderItems);
    }

    private boolean deductItems(Map<Long, Integer> orderItems) {
        return invokeWarehouseService("/books/deduct", orderItems);
    }

    private boolean isQuantityAvailable(Map<Long, Integer> orderItems) {
        return invokeWarehouseService("/books/validate", orderItems);
    }

    private boolean invokeWarehouseService(String endpoint, Map<Long, Integer> orderItems) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<Long, Integer>> requestEntity = new HttpEntity<>(orderItems, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                GATEWAY_SERVICE_URL + endpoint,
                HttpMethod.POST,
                requestEntity,
                Boolean.class
        );
        return Boolean.TRUE.equals(response.getBody());
    }
}

