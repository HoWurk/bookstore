package com.securemicroservices.bookstore.service;

import com.securemicroservices.bookstore.dto.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO getBookById(Long bookId);

    List<BookDTO> getAllBooks();

    BookDTO createBook(BookDTO bookDTO);

    BookDTO updateBook(Long bookId, BookDTO bookDTO);

    void deleteBook(Long bookId);
}
