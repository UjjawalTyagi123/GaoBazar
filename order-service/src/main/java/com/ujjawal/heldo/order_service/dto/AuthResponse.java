package com.ujjawal.heldo.order_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private Long userId;

    private String phoneNumber;

    private boolean newUser;

    private boolean profileCompleted;

    private String name;

    private String profilePhoto;
}