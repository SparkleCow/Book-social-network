package com.sparklecow.book.services.Mappers;

import com.sparklecow.book.dto.book.BookRequestDto;
import com.sparklecow.book.entities.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMapper {


    public Book toBook(BookRequestDto bookRequestDto) {
        return Book.builder()
                .title(bookRequestDto.title())
                .authorName(bookRequestDto.authorName())
                .synopsis(bookRequestDto.synopsis())
                .archived(false)
                .shareable(bookRequestDto.shareable())
                .isbn(bookRequestDto.isbn())
                .build();
    }
}
