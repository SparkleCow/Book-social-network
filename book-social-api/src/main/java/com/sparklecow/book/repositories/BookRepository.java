package com.sparklecow.book.repositories;

import com.sparklecow.book.entities.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("""
            SELECT book
            FROM Book book
            WHERE book.shareable = true
            AND book.archived = false
            AND book.owner.id != :id
            """)
    Page<Book> findAllDisplayableBooks(Integer id, Pageable pageable);

    @Query("""
            SELECT book
            FROM Book book
            WHERE book.owner.id = :id
            """)
    Page<Book> findBooksByOwnerId(Integer id, Pageable pageable);
}
