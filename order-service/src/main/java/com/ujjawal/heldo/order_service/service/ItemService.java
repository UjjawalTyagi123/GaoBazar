package com.ujjawal.heldo.order_service.service;

import com.ujjawal.heldo.order_service.dto.MyItemResponse;
import com.ujjawal.heldo.order_service.entity.Item;
import com.ujjawal.heldo.order_service.entity.ItemStatus;
import com.ujjawal.heldo.order_service.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final Logger log =
            LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository repository;
    private final ImageModerationService moderationService;

    /**
     * Create new item
     */
    public Item save(Long userId, Item item) {

        log.info("Saving item | villageId={} | title={}",
                item.getVillageId(),
                item.getTitle());

        moderationService.validate(item.getImageUrl());

        item.setUserId(userId);

        item.setStatus(ItemStatus.ACTIVE);

        return repository.save(item);
    }

    /**
     * Marketplace items
     * Only ACTIVE items are visible.
     */
    public Page<Item> getByVillage(
            Long villageId,
            Long districtId,
            String category,
            String scope,
            int page,
            int size) {

        log.info(
                "Fetching items | villageId={} | page={} | size={}",
                villageId,
                page,
                size);
        if ("VILLAGE".equals(scope)) {
            // query by villageId
            return repository.findByVillageIdAndCategoryAndStatusOrderByCreatedAtDesc(
                    villageId,
                    category,
                    ItemStatus.ACTIVE,
                    PageRequest.of(page, size)
            );
        } else {
            // query by districtId
            return repository.findByDistrictIdAndCategoryAndStatusOrderByCreatedAtDesc(
                    districtId,
                    category,
                    ItemStatus.ACTIVE,
                    PageRequest.of(page, size)
            );
        }

    }

    /**
     * Logged-in user's own items
     */
    public Page<MyItemResponse> getMyItems(
            Long userId,
            int page,
            int size) {

        Page<Item> items =
                repository.findByUserIdOrderByCreatedAtDesc(
                        userId,
                        PageRequest.of(page, size));

        return items.map(item ->
                MyItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .imageUrl(item.getImageUrl())
                        .category(item.getCategory())
                        .price(item.getPrice())
                        .status(item.getStatus())
                        .createdAt(item.getCreatedAt())
                        .build());
    }

    /**
     * Soft delete
     */
    @Transactional
    public void deleteItem(
            Long userId,
            Long itemId) {

        Item item =
                repository.findByIdAndUserId(
                                itemId,
                                userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Item not found"));

        item.setStatus(ItemStatus.DELETED);

        log.info(
                "Item deleted | userId={} | itemId={}",
                userId,
                itemId);
    }

    /**
     * Mark item as sold
     */
    @Transactional
    public void markAsSold(
            Long userId,
            Long itemId) {

        Item item =
                repository.findByIdAndUserId(
                                itemId,
                                userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Item not found"));

        item.setStatus(ItemStatus.SOLD);

        log.info(
                "Item marked SOLD | userId={} | itemId={}",
                userId,
                itemId);
    }
}