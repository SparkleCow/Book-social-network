package com.sparklecow.book.services;

import com.sparklecow.book.dto.book.BookRequestDto;
import com.sparklecow.book.dto.book.BookResponseDto;
import com.sparklecow.book.dto.book.BorrowedBookResponse;
import com.sparklecow.book.entities.book.Book;
import com.sparklecow.book.entities.bookTransaction.BookTransactionHistory;
import com.sparklecow.book.entities.user.User;
import com.sparklecow.book.exceptions.IllegalOperationException;
import com.sparklecow.book.exceptions.OperationNotPermittedException;
import com.sparklecow.book.models.PageResponse;
import com.sparklecow.book.repositories.BookRepository;
import com.sparklecow.book.repositories.BookTransactionHistoryRepository;
import com.sparklecow.book.services.Mappers.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private static final String CREATED_DATE = "createdDate";

    private final BookTransactionHistoryRepository bookTransactionRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    public Integer saveBook(BookRequestDto bookRequestDto, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(bookRequestDto);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponseDto findBookById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    public PageResponse<BookResponseDto> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(CREATED_DATE).descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(user.getId(), pageable);
        List<BookResponseDto> bookResponse = books.stream().map(bookMapper::toBookResponseDto).toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalPages(),
                books.getTotalElements(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponseDto> findBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(CREATED_DATE).descending());
        Page<Book> books = bookRepository.findBooksByOwnerId(user.getId(), pageable);
        List<BookResponseDto> bookResponse = books.stream().map(bookMapper::toBookResponseDto).toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalPages(),
                books.getTotalElements(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findBooksBorrowed(Integer page, Integer size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(CREATED_DATE).descending());
        Page<BookTransactionHistory> histories = bookTransactionRepository.findBooksBorrowed(user.getId(), pageable);
        List<BorrowedBookResponse> bookHistoriesResponse = histories.stream().map(bookMapper::toBorrowedBookResponse).toList();
        return new PageResponse<>(
                bookHistoriesResponse,
                histories.getNumber(),
                histories.getSize(),
                histories.getTotalPages(),
                histories.getTotalElements(),
                histories.isFirst(),
                histories.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findBooksReturned(Integer page, Integer size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(CREATED_DATE).descending());
        Page<BookTransactionHistory> histories = bookTransactionRepository.findBooksReturned(user.getId(), pageable);
        List<BorrowedBookResponse> bookHistoriesResponse = histories.stream().map(bookMapper::toBorrowedBookResponse).toList();
        return new PageResponse<>(
                bookHistoriesResponse,
                histories.getNumber(),
                histories.getSize(),
                histories.getTotalPages(),
                histories.getTotalElements(),
                histories.isFirst(),
                histories.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) throws IllegalOperationException {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: "+bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!book.getOwner().getId().equals(user.getId())){
            throw new IllegalOperationException("You are not the owner of this book");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) throws IllegalOperationException {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: "+bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!book.getOwner().getId().equals(user.getId())){
            throw new IllegalOperationException("You are not the owner of this book");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) throws OperationNotPermittedException, IllegalOperationException {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: "+bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Book is not available for borrowing");
        }
        User user = (User) connectedUser.getPrincipal();
        if(!book.getOwner().getId().equals(user.getId())){
            throw new IllegalOperationException("You are not the owner of this book");
        }
        boolean isBorrowed = bookTransactionRepository.isAlreadyBorrowedByUser(bookId, user.getId());
    }
}
