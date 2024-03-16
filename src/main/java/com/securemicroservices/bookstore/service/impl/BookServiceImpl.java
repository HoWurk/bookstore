package com.securemicroservices.bookstore.service.impl;

import com.securemicroservices.bookstore.dto.BookDTO;
import com.securemicroservices.bookstore.dto.converter.BookConverter;
import com.securemicroservices.bookstore.entity.Book;
import com.securemicroservices.bookstore.repository.BookRepository;
import com.securemicroservices.bookstore.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setPrice(bookDTO.getPrice());
        existingBook.setQuantityAvailable(bookDTO.getQuantityAvailable());
        return bookConverter.mapBookToDTO(bookRepository.save(existingBook));
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}

