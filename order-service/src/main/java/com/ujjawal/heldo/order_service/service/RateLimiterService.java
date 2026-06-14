package com.ujjawal.heldo.order_service.service;

import com.ujjawal.heldo.order_service.entity.DeviceRateLimit;
import com.ujjawal.heldo.order_service.repository.DeviceRateLimitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RateLimiterService {

    private static final Logger log =
            LoggerFactory.getLogger(RateLimiterService.class);

    private final DeviceRateLimitRepository repository;

    public RateLimiterService(DeviceRateLimitRepository repository) {
        this.repository = repository;
    }

    public void validatePost(String deviceId) {

        log.info("Rate limit validation started | deviceId={}", deviceId);

        DeviceRateLimit device = repository
                .findById(deviceId)
                .orElseGet(() -> {
                    log.info("New device detected | deviceId={}", deviceId);
                    DeviceRateLimit d = new DeviceRateLimit();
                    d.setDeviceId(deviceId);
                    return d;
                });

        LocalDate today = LocalDate.now();

        if (device.getLastPostDate() == null ||
                !device.getLastPostDate().equals(today)) {

            log.info("Resetting daily counter | deviceId={}", deviceId);
            device.setPostCount(0);
            device.setLastPostDate(today);
        }

        if (device.getLastPostTime() != null &&
                Duration.between(device.getLastPostTime(),
                        LocalDateTime.now()).toSeconds() < 5) {

            log.warn("Rate limit violation (2-min rule) | deviceId={}",
                    deviceId);

            throw new RuntimeException(
                    "Wait 2 minutes before posting again.");
        }

        if (device.getPostCount() >= 50) {

            log.warn("Daily post limit reached | deviceId={} | count={}",
                    deviceId, device.getPostCount());

            throw new RuntimeException("Daily post limit reached.");
        }

        device.setPostCount(device.getPostCount() + 1);
        device.setLastPostTime(LocalDateTime.now());

        repository.save(device);

        log.info("Rate limit updated | deviceId={} | newCount={}",
                deviceId, device.getPostCount());
    }
}
