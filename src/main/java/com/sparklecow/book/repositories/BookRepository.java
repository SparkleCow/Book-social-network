package com.sparklecow.book.repositories;

import com.sparklecow.book.entities.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
