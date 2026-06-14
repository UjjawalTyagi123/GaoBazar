package com.ujjawal.heldo.order_service.controller;
import com.ujjawal.heldo.order_service.dto.DistrictDto;
import com.ujjawal.heldo.order_service.dto.StateDto;
import com.ujjawal.heldo.order_service.dto.VillageDto;
import com.ujjawal.heldo.order_service.entity.District;
import com.ujjawal.heldo.order_service.entity.State;
import com.ujjawal.heldo.order_service.entity.Village;
import com.ujjawal.heldo.order_service.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/states")
    public ResponseEntity<List<StateDto>> getStates() {
        return ResponseEntity.ok(locationService.getAllStates());
    }

    @GetMapping("/districts/{stateId}")
    public ResponseEntity<List<DistrictDto>> getDistricts(@PathVariable Long stateId) {
        return ResponseEntity.ok(locationService.getDistrictsByState(stateId));
    }

    @GetMapping("/villages/{districtId}")
    public ResponseEntity<List<VillageDto>> getVillages(@PathVariable Long districtId) {
        return ResponseEntity.ok(locationService.getVillagesByDistrict(districtId));
    }
}