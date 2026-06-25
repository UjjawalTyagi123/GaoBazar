package com.ujjawal.heldo.order_service.controller;


import com.ujjawal.heldo.order_service.dto.AuthResponse;
import com.ujjawal.heldo.order_service.dto.FirebaseLoginRequest;
import com.ujjawal.heldo.order_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/firebase")
    public AuthResponse login(
            @RequestBody
            FirebaseLoginRequest request)
            throws Exception {

        return authService.login(request);
    }
}
