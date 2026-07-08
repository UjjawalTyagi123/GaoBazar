package com.ujjawal.heldo.order_service.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_village_created", columnList = "villageId, createdAt")
})
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable =false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status = ItemStatus.ACTIVE;
    private String title;
    private String description;
    private Double price;
    private String userName;
    private String imageUrl;
    private String phoneNumber;
    private String category;
    private String villageNameStr;
    private String districtStr;
    private String stateStr;
    private Long villageId;
    private Long districtId;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // getters & setters
}