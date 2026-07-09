package com.ujjawal.heldo.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {

    private String type;

    private String message;
}
