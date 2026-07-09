package com.ujjawal.heldo.order_service.service;

import com.ujjawal.heldo.order_service.dto.FeedbackRequest;
import com.ujjawal.heldo.order_service.entity.Feedback;
import com.ujjawal.heldo.order_service.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository repository;

    public void submit(
            Long userId,
            FeedbackRequest request
    ) {

        Feedback feedback =
                Feedback.builder()
                        .userId(userId)
                        .type(request.getType())
                        .message(request.getMessage())
                        .createdAt(LocalDateTime.now())
                        .build();

        repository.save(feedback);
    }
}
