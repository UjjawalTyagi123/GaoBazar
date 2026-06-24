package com.ujjawal.heldo.order_service.repository;

import com.ujjawal.heldo.order_service.entity.VillageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VillageRequestRepository
        extends JpaRepository<VillageRequest, Long> {

    boolean existsByStateIdAndDistrictNameIgnoreCaseAndVillageNameIgnoreCase(
            Long stateId,
            String districtName,
            String villageName
    );
}
