package com.securemicroservices.controller;

import com.securemicroservices.dto.OrderItemDTO;
import com.securemicroservices.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
@AllArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long orderItemId) {
        OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(orderItemId);
        return ResponseEntity.ok(orderItemDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        List<OrderItemDTO> orderItemDTOs = orderItemService.getAllOrderItems();
        return ResponseEntity.ok(orderItemDTOs);
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        OrderItemDTO createdOrderItem = orderItemService.createOrderItem(orderItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItem);
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Long orderItemId,
                                                        @RequestBody OrderItemDTO orderItemDTO) {
        OrderItemDTO updatedOrderItem = orderItemService.updateOrderItem(orderItemId, orderItemDTO);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }
}

