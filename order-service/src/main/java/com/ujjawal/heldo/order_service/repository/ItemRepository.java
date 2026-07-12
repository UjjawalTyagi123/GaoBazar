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


    @Query("""
SELECT i
FROM Item i
WHERE i.villageId = :villageId
AND i.category = :category
AND i.status = :status
AND (
LOWER(i.title) LIKE LOWER(CONCAT('%', :search, '%'))
OR
LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%'))
)
ORDER BY i.createdAt DESC
""")
    Page<Item> searchVillageItems(
            Long villageId,
            String category,
            String search,
            ItemStatus status,
            Pageable pageable
    );

    @Query("""
SELECT i
FROM Item i
WHERE i.districtId = :districtId
AND i.category = :category
AND i.status = :status
AND (
LOWER(i.title) LIKE LOWER(CONCAT('%', :search, '%'))
OR
LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%'))
)
ORDER BY i.createdAt DESC
""")
    Page<Item> searchDistrictItems(
            Long districtId,
            String category,
            String search,
            ItemStatus status,
            Pageable pageable
    );
}