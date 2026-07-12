package com.ujjawal.heldo.order_service.service;

import com.ujjawal.heldo.order_service.dto.MyItemResponse;
import com.ujjawal.heldo.order_service.entity.Item;
import com.ujjawal.heldo.order_service.entity.ItemStatus;
import com.ujjawal.heldo.order_service.entity.ItemView;
import com.ujjawal.heldo.order_service.repository.ItemRepository;
import com.ujjawal.heldo.order_service.repository.ItemViewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final Logger log =
            LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository repository;
    private final ItemViewRepository itemViewRepository;
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
            String search,
            int page,
            int size) {

        log.info(
                "Fetching items | scope={} | villageId={} | districtId={} | category={} | search={} | page={} | size={}",
                scope,
                villageId,
                districtId,
                category,
                search,
                page,
                size);

        boolean hasSearch =
                search != null &&
                        !search.trim().isEmpty();

        if ("VILLAGE".equals(scope)) {

            if (hasSearch) {

                return repository.searchVillageItems(
                        villageId,
                        category,
                        search.trim(),
                        ItemStatus.ACTIVE,
                        PageRequest.of(page, size)
                );
            }

            return repository
                    .findByVillageIdAndCategoryAndStatusOrderByCreatedAtDesc(
                            villageId,
                            category,
                            ItemStatus.ACTIVE,
                            PageRequest.of(page, size)
                    );
        }

        // DISTRICT scope

        if (hasSearch) {

            return repository.searchDistrictItems(
                    districtId,
                    category,
                    search.trim(),
                    ItemStatus.ACTIVE,
                    PageRequest.of(page, size)
            );
        }

        return repository
                .findByDistrictIdAndCategoryAndStatusOrderByCreatedAtDesc(
                        districtId,
                        category,
                        ItemStatus.ACTIVE,
                        PageRequest.of(page, size)
                );
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
                        .viewCount(item.getViewCount())
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

    @Transactional
    public void registerView(
            Long itemId,
            Long viewerId
    ) {

        Item item = repository.findById(itemId)
                .orElseThrow();

        // Don't count owner's own views
        if(item.getUserId().equals(viewerId)){
            return;
        }

        boolean alreadyViewed =
                itemViewRepository.existsByItemIdAndUserId(
                        itemId,
                        viewerId
                );

        if(alreadyViewed){
            return;
        }

        itemViewRepository.save(
                ItemView.builder()
                        .itemId(itemId)
                        .userId(viewerId)
                        .viewedAt(LocalDateTime.now())
                        .build()
        );

        repository.incrementViewCount(itemId);
    }

    @Transactional
    public Item updateItem(
            Long userId,
            Long itemId,
            Item updated
    ) {

        Item item =
                repository.findByIdAndUserId(
                        itemId,
                        userId
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Item not found"
                        )
                );

        item.setTitle(updated.getTitle());
        item.setDescription(updated.getDescription());
        item.setPrice(updated.getPrice());
        item.setPhoneNumber(updated.getPhoneNumber());
        item.setUserName(updated.getUserName());

        if (updated.getImageUrl() != null) {
            item.setImageUrl(updated.getImageUrl());
        }

        return repository.save(item);
    }
}