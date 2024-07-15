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
           SELECT history
           FROM BookTransactionHistory history
           WHERE history.user.id = :id
            """)
    Page<BookTransactionHistory> findBooksBorrowed(Integer id, Pageable pageable);

    @Query("""
           SELECT history
           FROM BookTransactionHistory history
           WHERE history.book.owner.id = :id
            """)
    Page<BookTransactionHistory> findBooksReturned(Integer id, Pageable pageable);

    @Query("""
            SELECT COUNT(history) > 0
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            AND history.book.id = :bookId
            AND history.returned = false
    """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
           SELECT history
           FROM BookTransactionHistory history
           WHERE history.book.id = :bookId
           AND history.user.id = :id
           AND history.returned = false
           AND history.returnApproved = false
            """)
    Optional<BookTransactionHistory> findBookTransactionByBookIdAndUserId(Integer bookId, Integer id);

    @Query("""
           SELECT history
           FROM BookTransactionHistory history
           WHERE history.book.id = :bookId
           AND history.book.owner.id = :id
           AND history.returned = false
           AND history.returnApproved = false
            """)
    Optional<BookTransactionHistory> findBookTransactionByBookIdAndOwnerId(Integer bookId, Integer id);
}
