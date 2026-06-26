package com.ujjawal.heldo.order_service.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.ujjawal.heldo.order_service.exception.InappropriateImageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
public class ImageModerationService {
    private static final Logger log =
            LoggerFactory.getLogger(ImageModerationService.class);
    private final GoogleCredentials credentials;

    public ImageModerationService(
            GoogleCredentials credentials) {
        this.credentials = credentials;
    }

    public void validate(String imageUrl) {
    log.info("Image processing under google validation");
        try {

            ByteString imgBytes =
                    ByteString.readFrom(
                            new URL(imageUrl).openStream()
                    );

            Image image =
                    Image.newBuilder()
                            .setContent(imgBytes)
                            .build();

            Feature feature =
                    Feature.newBuilder()
                            .setType(
                                    Feature.Type.SAFE_SEARCH_DETECTION
                            )
                            .build();

            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder()
                            .setImage(image)
                            .addFeatures(feature)
                            .build();

            ImageAnnotatorSettings settings =
                    ImageAnnotatorSettings.newBuilder()
                            .setCredentialsProvider(
                                    () -> credentials
                            )
                            .build();

            try (ImageAnnotatorClient client =
                         ImageAnnotatorClient.create(settings)) {

                BatchAnnotateImagesResponse response =
                        client.batchAnnotateImages(
                                List.of(request)
                        );

                SafeSearchAnnotation safe =
                        response.getResponses(0)
                                .getSafeSearchAnnotation();
                    log.info("safe seach value:{}",safe);
                if (safe.getAdultValue() >= Likelihood.LIKELY_VALUE) {
                    throw new InappropriateImageException(
                            "Adult content is not allowed."
                    );
                }

                if (safe.getViolenceValue() >= Likelihood.LIKELY_VALUE) {
                    throw new InappropriateImageException(
                            "Violent images are not allowed."
                    );
                }

                if (safe.getRacyValue() >= Likelihood.LIKELY_VALUE) {
                    throw new InappropriateImageException(
                            "Inappropriate images are not allowed."
                    );
                }
            }

        } catch (InappropriateImageException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "Unable to verify image." +  e.getMessage()
            );
        }
    }
}