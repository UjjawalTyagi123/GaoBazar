package com.ujjawal.heldo.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {

    private String name;

    private String profileImageUrl;

}