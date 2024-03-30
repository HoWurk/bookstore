package com.securemicroservices.service;

import com.securemicroservices.dto.BookDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface BookService {
    BookDTO getBookById(Long bookId);

    List<BookDTO> getAllBooks();

    BookDTO createBook(BookDTO bookDTO);

    BookDTO updateBook(Long bookId, BookDTO bookDTO);

    void deleteBook(Long bookId);

    boolean checkAvailability(Long bookId, int quantity);

    boolean checkMultipleAvailability(Map<Long, Integer> orderItems);

    boolean holdOrderItems(Map<Long, Integer> orderItems);

    void holdItem(Long bookId, int quantity);

    @Transactional
    boolean releaseOrderItems(Map<Long, Integer> orderItems);

    @Transactional
    void releaseItem(Long bookId, int quantity);

    void deductBookQuantity(Long bookId, int quantity);

    void addBookQuantity(Long bookId, int quantity);

    @Transactional
    boolean deductOrderItems(Map<Long, Integer> orderItems);
}
