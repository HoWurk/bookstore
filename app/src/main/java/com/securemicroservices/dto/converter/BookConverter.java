package com.securemicroservices.dto.converter;

import com.securemicroservices.dto.BookDTO;
import com.securemicroservices.entity.Book;
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
                .quantityOnHold(book.getQuantityOnHold())
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
                .quantityOnHold(bookDTO.getQuantityOnHold())
                .build();
    }
}
