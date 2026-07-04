package com.ujjawal.heldo.order_service.controller;

import com.ujjawal.heldo.order_service.dto.ProfileResponse;
import com.ujjawal.heldo.order_service.dto.ProfileSetupRequest;
import com.ujjawal.heldo.order_service.dto.UpdateProfileRequest;
import com.ujjawal.heldo.order_service.service.JwtService;
import com.ujjawal.heldo.order_service.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;

    private Long getUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }

    @PostMapping("/setup")
    public ProfileResponse setupProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ProfileSetupRequest request) {

        return profileService.setupProfile(
                getUserId(authorization),
                request
        );
    }

    @GetMapping("/me")
    public ProfileResponse getProfile(
            @RequestHeader("Authorization") String authorization) {

        return profileService.getProfile(
                getUserId(authorization)
        );
    }

    @PutMapping
    public ProfileResponse updateProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UpdateProfileRequest request) {

        return profileService.updateProfile(
                getUserId(authorization),
                request
        );
    }
}