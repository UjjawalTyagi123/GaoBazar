package com.ujjawal.heldo.order_service.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Component
public class FirebaseInitializer {

    @Value("${FIREBASE_SERVICE_ACCOUNT:NOT_FOUND}")
    private String firebaseConfig;

    @PostConstruct
    public void initialize() {
        System.out.println(
                "Firebase variable starts with: " +
                        firebaseConfig.substring(
                                0,
                                Math.min(20, firebaseConfig.length())
                        )
        );
        try {

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(
                                    GoogleCredentials.fromStream(
                                            new ByteArrayInputStream(
                                                    firebaseConfig.getBytes(
                                                            StandardCharsets.UTF_8
                                                    )
                                            )
                                    )
                            )
                            .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}