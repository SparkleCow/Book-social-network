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
import com.sparklecow.book.services.file.FileStorageService;
import com.sparklecow.book.services.mappers.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private static final String CREATED_DATE = "createdDate";
    private static final String BOOK_NOT_FOUND = "Book not found with id: ";

    private final BookTransactionHistoryRepository bookTransactionRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final FileStorageService fileStorageService;

    public Integer saveBook(BookRequestDto bookRequestDto, Authentication connectedUser) throws IllegalOperationException {
        User user = (User) connectedUser.getPrincipal();
        Book book;
        if(bookRequestDto.id() != null){
            book = bookRepository.findById(bookRequestDto.id()).orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND + bookRequestDto.id()));
            if(!user.getId().equals(book.getOwner().getId())){
                throw new IllegalOperationException("You are not the owner of this book. You can't update it");
            }
            Book newBook = bookMapper.toBookUpdated(bookRequestDto);
            newBook.setOwner(user);
            return bookRepository.save(newBook).getId();
        }
        book = bookMapper.toBook(bookRequestDto);
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

    //Method for return all books borrowed by user.
    public PageResponse<BorrowedBookResponse> findBooksBorrowedByUser(Integer page, Integer size, Authentication connectedUser) {
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
                () -> new EntityNotFoundException(BOOK_NOT_FOUND+bookId));
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
                () -> new EntityNotFoundException(BOOK_NOT_FOUND+bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!book.getOwner().getId().equals(user.getId())){
            throw new IllegalOperationException("You are not the owner of this book");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    /*Method allows borrow a book.
    Firstly it validate if the book is shareable, otherwise it will send an error
    On the other hand, it will validate if the user is the book's owner, in that case it will throw an error*/
    public Integer borrowBook(Integer bookId, Authentication connectedUser) throws OperationNotPermittedException, IllegalOperationException {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND+bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Book is not available for borrowing");
        }
        User user = (User) connectedUser.getPrincipal();
        if(book.getOwner().getId().equals(user.getId())){
            throw new IllegalOperationException("You can´t borrow your own book");
        }
        final boolean isBorrowed = bookTransactionRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if(isBorrowed){
            throw new OperationNotPermittedException("Book is already borrowed");
        }
        final boolean isNotApproved = bookTransactionRepository.isAlreadyBorrowed(bookId, user.getId());
        if (isNotApproved) {
            throw new OperationNotPermittedException("The owner has not yet approved the return of the book. You cant borrow it until the owner approves it");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory
                .builder()
                .book(book)
                .user(user)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) throws OperationNotPermittedException, IllegalOperationException {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND+bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Book is not available for borrowing");
        }
        User user = (User) connectedUser.getPrincipal();
        if(book.getOwner().getId().equals(user.getId())){
            throw new IllegalOperationException("You can´t return your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionRepository
                .findBookTransactionByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book not borrowed by user"));
        bookTransactionHistory.setReturned(true);
        return bookTransactionRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) throws OperationNotPermittedException, IllegalOperationException {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND+bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Book is not available for borrowing");
        }
        User user = (User) connectedUser.getPrincipal();
        if(!book.getOwner().getId().equals(user.getId())){
            throw new IllegalOperationException("You can´t approve the return of a book you do not own");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionRepository
                .findBookTransactionByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("The book is not returned yet. You cannot approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionRepository.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverPicture(Integer bookId, MultipartFile file, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND+bookId));
        User user = (User) connectedUser.getPrincipal();
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}
