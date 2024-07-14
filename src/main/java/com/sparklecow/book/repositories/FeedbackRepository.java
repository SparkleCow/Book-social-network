package com.sparklecow.book.repositories;

import com.sparklecow.book.entities.feedback.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("""
            SELECT feedback
            FROM Feedback feedback
            where feedback.book.id = :bookId
            """)

    Page<Feedback> findAllByBook(Pageable pageable, Integer bookId);
}