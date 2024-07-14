package com.sparklecow.book.services.feedback;

import com.sparklecow.book.dto.feedback.FeedbackRequestDto;
import com.sparklecow.book.dto.feedback.FeedbackResponseDto;
import com.sparklecow.book.entities.book.Book;
import com.sparklecow.book.entities.feedback.Feedback;
import com.sparklecow.book.entities.user.User;
import com.sparklecow.book.exceptions.IllegalOperationException;
import com.sparklecow.book.exceptions.OperationNotPermittedException;
import com.sparklecow.book.models.PageResponse;
import com.sparklecow.book.repositories.BookRepository;
import com.sparklecow.book.repositories.FeedbackRepository;
import com.sparklecow.book.services.mappers.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    public Integer saveFeedback(FeedbackRequestDto feedbackRequestDto, Authentication connectedUser) throws OperationNotPermittedException, IllegalOperationException {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(feedbackRequestDto.bookId()).orElseThrow(
                () -> new RuntimeException("Book not found"));
        if(!book.isShareable() || book.isArchived()) {
            throw new OperationNotPermittedException("Book is not shareable or archived");
        }
        if(book.getOwner().getId().equals(user.getId())) {
            throw new IllegalOperationException("You cannot give feedback to your own book");
        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequestDto, book);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponseDto> findAllFeedbacksByBook(Integer bookId, Integer page, Integer size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreatedDate").descending());
        Page<Feedback> feedbacks = feedbackRepository.findAllByBook(pageable, bookId);
        List<FeedbackResponseDto> feedbackResponseDtos = feedbacks.stream().map(feedback ->
                feedbackMapper.toFeedbackResponseDto(feedback, user.getId())).toList();
        return new PageResponse<>(
                feedbackResponseDtos,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalPages(),
                feedbacks.getTotalElements(),
                feedbacks.isFirst(),
                feedbacks.isLast());
    }

}
