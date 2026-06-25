package com.ujjawal.heldo.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private Long userId;

    private String phoneNumber;
}
