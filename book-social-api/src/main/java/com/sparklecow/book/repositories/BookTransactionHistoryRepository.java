package com.sparklecow.book.repositories;

import com.sparklecow.book.entities.bookTransaction.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
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

    @Query("""
           SELECT (COUNT(*)>0) AS bookTransactionHistory
           FROM BookTransactionHistory history
           WHERE history.book.owner.id = :userId
           AND history.book.id = :bookId
           AND history.returned = false
            """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
           SELECT bookTransactionHistory
           FROM BookTransactionHistory history
           WHERE history.book.id = :bookId
           AND history.user.id = :id
           AND history.returned = false
           AND history.returnApproved
            """)
    Optional<BookTransactionHistory> findBookTransactionByBookIdAndUserId(Integer bookId, Integer id);

    @Query("""
           SELECT bookTransactionHistory
           FROM BookTransactionHistory history
           WHERE history.book.id = :bookId
           AND history.book.owner.id = :id
           AND history.returned = false
           AND history.returnApproved
            """)
    Optional<BookTransactionHistory> findBookTransactionByBookIdAndOwnerId(Integer bookId, Integer id);
}
