package com.ujjawal.heldo.order_service.service;

import com.ujjawal.heldo.order_service.dto.VillageRequestDto;
import com.ujjawal.heldo.order_service.entity.RequestStatus;
import com.ujjawal.heldo.order_service.entity.VillageRequest;
import com.ujjawal.heldo.order_service.repository.VillageRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VillageRequestService {

    private final VillageRequestRepository repository;

    public String submit(VillageRequestDto dto) {

        boolean exists = repository
                .existsByStateIdAndDistrictNameIgnoreCaseAndVillageNameIgnoreCase(
                        dto.getStateId(),
                        dto.getDistrictName(),
                        dto.getVillageName()
                );

        if (exists) {
            return "Request already submitted.";
        }

        VillageRequest request = VillageRequest.builder()
                .stateId(dto.getStateId())
                .stateName(dto.getStateName())
                .districtName(dto.getDistrictName())
                .villageName(dto.getVillageName())
                .remarks(dto.getRemarks())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(request);

        return "Request submitted successfully.";
    }
}
