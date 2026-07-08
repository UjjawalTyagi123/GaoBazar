package com.ujjawal.heldo.order_service.repository;

import com.ujjawal.heldo.order_service.entity.Item;
import com.ujjawal.heldo.order_service.entity.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByVillageIdAndCategoryAndStatusOrderByCreatedAtDesc(
            Long villageId,
            String category,
            ItemStatus status,
            Pageable pageable
    );
    Page<Item> findByDistrictIdAndCategoryAndStatusOrderByCreatedAtDesc(
            Long districtId,
            String category,
            ItemStatus status,
            Pageable pageable
    );


    Optional<Item> findByIdAndUserId(
            Long itemId,
            Long userId
    );

    Page<Item> findByUserIdOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );

    @Modifying
    @Query("""
UPDATE Item i
SET i.viewCount = i.viewCount + 1
WHERE i.id = :itemId
""")
    void incrementViewCount(Long itemId);

}