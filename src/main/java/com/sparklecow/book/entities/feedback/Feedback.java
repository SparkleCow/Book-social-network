package com.sparklecow.book.entities.feedback;

import com.sparklecow.book.entities.book.Book;
import com.sparklecow.book.entities.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "feedbacks")
public class Feedback extends BaseEntity {

    private Double note;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
