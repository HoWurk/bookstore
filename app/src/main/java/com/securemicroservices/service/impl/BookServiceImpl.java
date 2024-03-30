package com.securemicroservices.service.impl;

import com.securemicroservices.dto.BookDTO;
import com.securemicroservices.dto.converter.BookConverter;
import com.securemicroservices.entity.Book;
import com.securemicroservices.repository.BookRepository;
import com.securemicroservices.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private BookConverter bookConverter;

    @Override
    public BookDTO getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + bookId));
        return bookConverter.mapBookToDTO(book);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookConverter::mapBookToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookConverter.mapDTOToBook(bookDTO);
        book = bookRepository.save(book);
        return bookConverter.mapBookToDTO(book);
    }

    @Override
    public BookDTO updateBook(Long bookId, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + bookId));
        updateExistingBook(bookDTO, existingBook);
        return bookConverter.mapBookToDTO(bookRepository.save(existingBook));
    }

    private static void updateExistingBook(BookDTO bookDTO, Book existingBook) {
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setPrice(bookDTO.getPrice());
        existingBook.setQuantityAvailable(bookDTO.getQuantityAvailable());
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public boolean checkAvailability(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + bookId));
        int currentQuantity = book.getQuantityAvailable();
        return currentQuantity >= quantity;
    }

    @Override
    public boolean checkMultipleAvailability(Map<Long, Integer> orderItems) {
        for (Map.Entry<Long, Integer> entry : orderItems.entrySet()) {
            Long itemId = entry.getKey();
            int quantity = entry.getValue();

            if (!checkAvailability(itemId, quantity)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean holdOrderItems(Map<Long, Integer> orderItems) {
        try {
            for (Map.Entry<Long, Integer> entry : orderItems.entrySet()) {
                Long itemId = entry.getKey();
                int quantity = entry.getValue();

                holdItem(itemId, quantity);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to hold order items", e);
        }
    }

    @Override
    public void holdItem(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + bookId));

        int availableQuantity = book.getQuantityAvailable();
        int onHoldQuantity = book.getQuantityOnHold();

        if (availableQuantity < quantity) {
            throw new IllegalArgumentException("Not enough quantity available to hold");
        }

        book.setQuantityAvailable(availableQuantity - quantity);
        book.setQuantityOnHold(onHoldQuantity + quantity);
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hold item with ID = " + bookId, e);
        }
    }

    @Override
    @Transactional
    public boolean releaseOrderItems(Map<Long, Integer> orderItems) {
        try {
            for (Map.Entry<Long, Integer> entry : orderItems.entrySet()) {
                Long itemId = entry.getKey();
                int quantity = entry.getValue();

                releaseItem(itemId, quantity);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to release order items", e);
        }
    }

    @Override
    public void releaseItem(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + bookId));

        int availableQuantity = book.getQuantityAvailable();
        int onHoldQuantity = book.getQuantityOnHold();

        if (onHoldQuantity < quantity) {
            throw new IllegalArgumentException("Not enough quantity available to release");
        }

        book.setQuantityAvailable(availableQuantity + quantity);
        book.setQuantityOnHold(onHoldQuantity - quantity);
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            throw new RuntimeException("Failed to release item with ID: " + bookId, e);
        }
    }

    @Override
    @Transactional
    public boolean deductOrderItems(Map<Long, Integer> orderItems) {
        try {
            for (Map.Entry<Long, Integer> entry : orderItems.entrySet()) {
                Long itemId = entry.getKey();
                int quantity = entry.getValue();

                deductBookQuantity(itemId, quantity);
            }
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deduct order items. Transaction will be rolled back.", e);
        }
    }

    @Override
    public void deductBookQuantity(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + bookId));

        int onHoldQuantity = book.getQuantityOnHold();

        if (onHoldQuantity < quantity) {
            throw new IllegalArgumentException("Not enough on hold quantity available to deduct");
        }

        book.setQuantityOnHold(onHoldQuantity - quantity);
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deduct from item with ID: " + bookId, e);
        }
    }

    @Override
    public void addBookQuantity(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + bookId));
        int currentQuantity = book.getQuantityAvailable();
        book.setQuantityAvailable(currentQuantity + quantity);
        bookRepository.save(book);
    }


    /*private void rollbackDeductions(Map<Long, Integer> successfulDeductions) {
        for (Map.Entry<Long, Integer> rollbackEntry : successfulDeductions.entrySet()) {
            Long rollbackItemId = rollbackEntry.getKey();
            int rollbackQuantity = rollbackEntry.getValue();
            addBookQuantity(rollbackItemId, rollbackQuantity);
        }
    }*/
}

