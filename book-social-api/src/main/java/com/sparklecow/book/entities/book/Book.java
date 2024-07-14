package com.sparklecow.book.entities.book;

import com.sparklecow.book.entities.bookTransaction.BookTransactionHistory;
import com.sparklecow.book.entities.common.BaseEntity;
import com.sparklecow.book.entities.feedback.Feedback;
import com.sparklecow.book.entities.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks = new ArrayList<>();
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> bookTransactionHistories = new ArrayList<>();

    @Transient
    public Double getRate(){
        if(this.feedbacks == null || this.feedbacks.isEmpty()) return 0.0;
        return this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
    }

}
