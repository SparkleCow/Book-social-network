package com.sparklecow.book.repositories;

import com.sparklecow.book.entities.bookTransaction.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {
    @Query("""
           SELECT bookTransactionHistory
           FROM BookTransactionHistory history
           WHERE history.user = :id
            """)
    Page<BookTransactionHistory> findBooksBorrowed(Integer id, Pageable pageable);

    @Query("""
           SELECT bookTransactionHistory
           FROM BookTransactionHistory history
           WHERE history.book.owner.id = :id
            """)
    Page<BookTransactionHistory> findBooksReturned(Integer id, Pageable pageable);

    boolean isAlreadyBorrowedByUser(Integer bookId, Integer id);
}
