package com.securemicroservices.controller;

import com.securemicroservices.dto.BookDTO;
import com.securemicroservices.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId) {
        BookDTO bookDTO = bookService.getBookById(bookId);
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long bookId, @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(bookId, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bookId}/availability")
    public ResponseEntity<Boolean> checkBookAvailability(@PathVariable Long bookId, @RequestParam int quantity) {
        boolean available = bookService.checkAvailability(bookId, quantity);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/{bookId}/add")
    public ResponseEntity<Void> addBookQuantity(@PathVariable Long bookId, @RequestParam int quantity) {
        bookService.addBookQuantity(bookId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateOrderAvailability(@RequestBody Map<Long, Integer> orderItems) {
        boolean available = bookService.checkMultipleAvailability(orderItems);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/hold")
    public ResponseEntity<Boolean> holdOrderItems(@RequestBody Map<Long, Integer> orderItems) {
        boolean held = bookService.holdOrderItems(orderItems);
        return ResponseEntity.ok(held);
    }

    @PostMapping("/release")
    public ResponseEntity<Boolean> releaseOrderItems(@RequestBody Map<Long, Integer> orderItems) {
        boolean released = bookService.releaseOrderItems(orderItems);
        return ResponseEntity.ok(released);
    }

    @PostMapping("/deduct")
    public ResponseEntity<Boolean> deductOrderItems(@RequestBody Map<Long, Integer> orderItems) {
        boolean successful = bookService.deductOrderItems(orderItems);
        return ResponseEntity.ok(successful);
    }

}

