package com.ujjawal.heldo.order_service.service;

import com.ujjawal.heldo.order_service.dto.ProfileResponse;
import com.ujjawal.heldo.order_service.dto.ProfileSetupRequest;
import com.ujjawal.heldo.order_service.dto.UpdateProfileRequest;
import com.ujjawal.heldo.order_service.entity.User;
import com.ujjawal.heldo.order_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileResponse setupProfile(
            Long userId,
            ProfileSetupRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getProfileCompleted())) {
            throw new RuntimeException("Profile already created.");
        }

        user.setName(request.getName());
        user.setProfilePhoto(request.getProfileImageUrl());
        user.setStateId(request.getStateId());
        user.setDistrictId(request.getDistrictId());
        user.setVillageId(request.getVillageId());

        user.setStateName(request.getStateName());
        user.setDistrictName(request.getDistrictName());
        user.setVillageName(request.getVillageName());

        user.setProfileCompleted(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return map(user);
    }

    public ProfileResponse getProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return map(user);
    }

    public ProfileResponse updateProfile(
            Long userId,
            UpdateProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (request.getName() != null &&
                !request.getName().isBlank()) {

            user.setName(request.getName().trim());
        }

        if (request.getProfileImageUrl() != null &&
                !request.getProfileImageUrl().isBlank()) {

            user.setProfilePhoto(
                    request.getProfileImageUrl().trim());
        }

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return map(user);
    }

    private ProfileResponse map(User user) {

        return ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(user.getProfilePhoto())

                .stateId(user.getStateId())
                .districtId(user.getDistrictId())
                .villageId(user.getVillageId())

                .stateName(user.getStateName())
                .districtName(user.getDistrictName())
                .villageName(user.getVillageName())

                .profileCompleted(user.getProfileCompleted())

                .build();
    }
}