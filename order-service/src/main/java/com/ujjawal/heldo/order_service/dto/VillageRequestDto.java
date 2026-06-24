package com.ujjawal.heldo.order_service.dto;

import lombok.Data;

@Data
public class VillageRequestDto {

    private Long stateId;

    private String stateName;

    private String districtName;

    private String villageName;

    private String remarks;
}
