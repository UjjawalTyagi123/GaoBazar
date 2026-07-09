package com.ujjawal.heldo.order_service.controller;

import com.ujjawal.heldo.order_service.dto.FeedbackRequest;
import com.ujjawal.heldo.order_service.service.FeedbackService;
import com.ujjawal.heldo.order_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final JwtService jwtService;

    @PostMapping
    public void submitFeedback(
            @RequestHeader("Authorization")
            String authorization,

            @RequestBody
            FeedbackRequest request
    ) {

        String token =
                authorization.replace(
                        "Bearer ",
                        ""
                );

        Long userId =
                jwtService.extractUserId(
                        token
                );

        feedbackService.submit(
                userId,
                request
        );
    }
}
