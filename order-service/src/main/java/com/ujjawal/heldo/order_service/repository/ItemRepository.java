package com.ujjawal.heldo.order_service.repository;

import com.ujjawal.heldo.order_service.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByVillageIdAndCategory(Long villageId,String category, Pageable pageable);
}