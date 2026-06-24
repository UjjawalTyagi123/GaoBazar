package com.ujjawal.heldo.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String phoneNumber;

    private String name;

    private Boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;
}