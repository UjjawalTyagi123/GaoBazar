package com.ujjawal.heldo.order_service.service;


import java.time.LocalDateTime;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import com.ujjawal.heldo.order_service.dto.AuthResponse;
import com.ujjawal.heldo.order_service.dto.FirebaseLoginRequest;

import com.ujjawal.heldo.order_service.entity.User;

import com.ujjawal.heldo.order_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse login(
            FirebaseLoginRequest request)
            throws Exception {

        FirebaseToken decodedToken =
                FirebaseAuth.getInstance()
                        .verifyIdToken(
                                request.getFirebaseToken());

        String uid =
                decodedToken.getUid();

        String phone =
                (String) decodedToken
                        .getClaims()
                        .get("phone_number");

        User user =
                userRepository
                        .findByFirebaseUid(uid)
                        .orElse(null);

        boolean isNewUser = false;

        if (user == null) {

            isNewUser = true;

            user =
                    User.builder()
                            .firebaseUid(uid)
                            .phoneNumber(phone)
                            .profileCompleted(false)
                            .createdAt(LocalDateTime.now())
                            .build();

            user =
                    userRepository.save(user);
        }

        String jwt =
                jwtService.generateToken(
                        user.getId());

        return AuthResponse.builder()
                .token(jwt)
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .newUser(isNewUser)
                .profileCompleted(
                        Boolean.TRUE.equals(
                                user.getProfileCompleted()))
                .name(user.getName())
                .profilePhoto(
                        user.getProfilePhoto())
                .build();
    }
}
