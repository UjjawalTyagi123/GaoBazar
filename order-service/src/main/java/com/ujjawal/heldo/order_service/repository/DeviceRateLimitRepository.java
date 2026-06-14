package com.ujjawal.heldo.order_service.repository;

import com.ujjawal.heldo.order_service.entity.DeviceRateLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRateLimitRepository
        extends JpaRepository<DeviceRateLimit, String> {
}