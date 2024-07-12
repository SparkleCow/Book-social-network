package com.sparklecow.book.services;

import com.sparklecow.book.dto.book.BookRequestDto;
import com.sparklecow.book.entities.book.Book;
import com.sparklecow.book.entities.user.User;
import com.sparklecow.book.repositories.BookRepository;
import com.sparklecow.book.services.Mappers.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    public Integer saveBook(BookRequestDto bookRequestDto, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(bookRequestDto);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }
}
