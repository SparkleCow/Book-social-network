package com.sparklecow.book.services.Mappers;

import com.sparklecow.book.dto.book.BookRequestDto;
import com.sparklecow.book.dto.book.BookResponseDto;
import com.sparklecow.book.dto.book.BorrowedBookResponse;
import com.sparklecow.book.entities.book.Book;
import com.sparklecow.book.entities.bookTransaction.BookTransactionHistory;
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

    public BookResponseDto toBookResponseDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .synopsis(book.getSynopsis())
                .isbn(book.getIsbn())
                .rate(book.getRate())
                .owner(book.getOwner().getFullName())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                //.cover() TODO
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory) {
        return BorrowedBookResponse.builder()
                .id(bookTransactionHistory.getBook().getId())
                .title(bookTransactionHistory.getBook().getTitle())
                .authorName(bookTransactionHistory.getBook().getAuthorName())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .rate(bookTransactionHistory.getBook().getRate())
                .returned(bookTransactionHistory.isReturned())
                .returnApproved(bookTransactionHistory.isReturnApproved())
                .build();
    }
}
