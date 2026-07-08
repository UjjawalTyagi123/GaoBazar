package com.ujjawal.heldo.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileSetupRequest {

    private String name;
    private String profileImageUrl;
    private Long stateId;
    private Long districtId;
    private Long villageId;

    private String stateName;
    private String districtName;
    private String villageName;

}