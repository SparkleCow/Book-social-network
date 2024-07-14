package com.sparklecow.book.controllers;

import com.sparklecow.book.dto.book.BookRequestDto;
import com.sparklecow.book.dto.book.BookResponseDto;
import com.sparklecow.book.dto.book.BorrowedBookResponse;
import com.sparklecow.book.exceptions.IllegalOperationException;
import com.sparklecow.book.exceptions.OperationNotPermittedException;
import com.sparklecow.book.models.PageResponse;
import com.sparklecow.book.services.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "Book")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @Valid @RequestBody BookRequestDto bookRequestDto,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.saveBook(bookRequestDto, connectedUser));
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponseDto> findBookById(
            @PathVariable("book-id") Integer bookId) {
        return ResponseEntity.ok(bookService.findBookById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponseDto>> findAllBooks(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponseDto>> findBooksByOwner(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findBooksBorrowed(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findBooksBorrowed(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findBooksReturned(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findBooksReturned(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("id") Integer bookId,
            Authentication connectedUser) throws IllegalOperationException {
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId, connectedUser));
    }

    @PatchMapping("/archived/{id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("id") Integer bookId,
            Authentication connectedUser) throws IllegalOperationException {
        return ResponseEntity.ok(bookService.updateArchivedStatus(bookId, connectedUser));
    }

    @PostMapping("/borrow/{id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("id") Integer bookId,
            Authentication connectedUser) throws IllegalOperationException, OperationNotPermittedException {
        return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/{id}")
    public ResponseEntity<Integer> returnBorrowedBook(
            @PathVariable("id") Integer bookId,
            Authentication connectedUser) throws IllegalOperationException, OperationNotPermittedException {
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{id}")
    public ResponseEntity<Integer> approveReturnBorrowedBook(
            @PathVariable("id") Integer bookId,
            Authentication connectedUser) throws IllegalOperationException, OperationNotPermittedException {
        return ResponseEntity.ok(bookService.approveReturnBorrowedBook(bookId, connectedUser));
    }

    @PostMapping(value="/cover/{id}", consumes="multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("id") Integer bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ){
        bookService.uploadBookCoverPicture(bookId, file, connectedUser);
        return ResponseEntity.accepted().build();
    }


}
