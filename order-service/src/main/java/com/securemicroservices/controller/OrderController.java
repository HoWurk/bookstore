package com.securemicroservices.controller;

import com.securemicroservices.dto.OrderDTO;
import com.securemicroservices.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orderDTOs = orderService.getAllOrders();
        return ResponseEntity.ok(orderDTOs);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId,
                                                @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(orderId, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/validate")
    public ResponseEntity<Boolean> validateOrder(@PathVariable Long orderId) {
        boolean validOrder = orderService.validateOrder(orderId);
        return ResponseEntity.ok(validOrder);
    }

    @GetMapping("/{orderId}/hold")
    public ResponseEntity<Boolean> holdItemsInOrder(@PathVariable Long orderId) {
        boolean validOrder = orderService.holdItemsInOrder(orderId);
        return ResponseEntity.ok(validOrder);
    }

    @GetMapping("/{orderId}/release")
    public ResponseEntity<Boolean> releaseItemsInOrder(@PathVariable Long orderId) {
        boolean validOrder = orderService.releaseItemsInOrder(orderId);
        return ResponseEntity.ok(validOrder);
    }

    @GetMapping("/{orderId}/complete")
    public ResponseEntity<Boolean> completeTransaction(@PathVariable Long orderId) {
        boolean validOrder = orderService.completeTransaction(orderId);
        return ResponseEntity.ok(validOrder);
    }
}
