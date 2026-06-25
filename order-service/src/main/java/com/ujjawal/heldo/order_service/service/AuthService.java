package com.ujjawal.heldo.order_service.service;


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
                        .verifyIdToken(request.getFirebaseToken());

        String uid = decodedToken.getUid();

        String phone =
                (String) decodedToken.getClaims()
                        .get("phone_number");

        User user =
                userRepository
                        .findByFirebaseUid(uid)
                        .orElseGet(() -> {

                            User u =
                                    User.builder()
                                            .firebaseUid(uid)
                                            .phoneNumber(phone)
                                            .createdAt(
                                                    LocalDateTime.now())
                                            .build();

                            return userRepository.save(u);
                        });

        String jwt =
                jwtService.generateToken(
                        user.getId());

        return new AuthResponse(
                jwt,
                user.getId(),
                user.getPhoneNumber());
    }
}
