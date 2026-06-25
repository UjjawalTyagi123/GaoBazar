package com.ujjawal.heldo.order_service.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class VisionConfig {

    @Value("${GOOGLE_VISION_CREDENTIALS}")
    private String credentials;

    @Bean
    public GoogleCredentials googleCredentials() throws Exception {
        return GoogleCredentials.fromStream(
                new ByteArrayInputStream(
                        credentials.getBytes(
                                StandardCharsets.UTF_8
                        )
                )
        );
    }
}