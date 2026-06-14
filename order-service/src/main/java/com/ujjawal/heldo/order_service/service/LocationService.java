package com.ujjawal.heldo.order_service.service;
import com.ujjawal.heldo.order_service.dto.DistrictDto;
import com.ujjawal.heldo.order_service.dto.StateDto;
import com.ujjawal.heldo.order_service.dto.VillageDto;
import com.ujjawal.heldo.order_service.entity.District;
import com.ujjawal.heldo.order_service.entity.State;
import com.ujjawal.heldo.order_service.entity.Village;
import com.ujjawal.heldo.order_service.repository.DistrictRepository;
import com.ujjawal.heldo.order_service.repository.StateRepository;
import com.ujjawal.heldo.order_service.repository.VillageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LocationService {

    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final VillageRepository villageRepository;

    public List<StateDto> getAllStates() {
        log.info("Fetching all states");
        return stateRepository.findAll().stream()
                .map(s -> new StateDto(s.getId(), s.getName()))
                .toList();
    }

    public List<DistrictDto> getDistrictsByState(Long stateId) {
        log.info("Fetching districts for stateId={}", stateId);
        return districtRepository.findByStateId(stateId).stream()
                .map(s -> new DistrictDto(s.getId(), s.getName()))
                .toList();
    }

    public List<VillageDto> getVillagesByDistrict(Long districtId) {
        log.info("Fetching villages for districtId={}", districtId);
        return villageRepository.findByDistrictId(districtId).stream()
                .map(s -> new VillageDto(s.getId(), s.getName()))
                .toList();
    }
}