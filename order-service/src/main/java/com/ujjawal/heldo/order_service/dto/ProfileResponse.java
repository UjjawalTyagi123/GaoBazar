package com.ujjawal.heldo.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProfileResponse {

    private Long id;

    private String name;

    private String phoneNumber;

    private String profileImageUrl;

    private Long stateId;
    private Long districtId;
    private Long villageId;

    private String stateName;
    private String districtName;
    private String villageName;

    private boolean profileCompleted;

}