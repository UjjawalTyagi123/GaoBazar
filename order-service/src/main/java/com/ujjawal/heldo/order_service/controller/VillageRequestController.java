package com.ujjawal.heldo.order_service.controller;

import com.ujjawal.heldo.order_service.dto.VillageRequestDto;
import com.ujjawal.heldo.order_service.service.VillageRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/village-requests")
@RequiredArgsConstructor
public class VillageRequestController {

    private final VillageRequestService service;

    @PostMapping
    public ResponseEntity<String> submit(
            @RequestBody VillageRequestDto dto) {

        return ResponseEntity.ok(
                service.submit(dto)
        );
    }
}
