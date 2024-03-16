package com.securemicroservices.bookstore.dto.converter;

import com.securemicroservices.bookstore.dto.BookDTO;
import com.securemicroservices.bookstore.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {
    public BookDTO mapBookToDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .quantityAvailable(book.getQuantityAvailable())
                .build();
    }

    public Book mapDTOToBook(BookDTO bookDTO) {
        return Book.builder()
                .id(bookDTO.getId())
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .description(bookDTO.getDescription())
                .isbn(bookDTO.getIsbn())
                .price(bookDTO.getPrice())
                .quantityAvailable(bookDTO.getQuantityAvailable())
                .build();
    }
}
