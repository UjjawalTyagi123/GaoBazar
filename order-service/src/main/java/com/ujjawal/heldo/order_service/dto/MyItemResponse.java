package com.ujjawal.heldo.order_service.dto;

import com.ujjawal.heldo.order_service.entity.ItemStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MyItemResponse {

    private Long id;

    private String title;

    private String imageUrl;

    private String category;

    private Double price;

    private ItemStatus status;

    private LocalDateTime createdAt;
}