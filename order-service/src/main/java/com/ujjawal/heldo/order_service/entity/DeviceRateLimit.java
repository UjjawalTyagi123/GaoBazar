package com.ujjawal.heldo.order_service.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class DeviceRateLimit {

    @Id
    private String deviceId;

    private int postCount;
    private LocalDate lastPostDate;
    private LocalDateTime lastPostTime;

    // getters & setters
}
