package com.ujjawal.heldo.order_service.repository;

import com.ujjawal.heldo.order_service.entity.ItemView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemViewRepository
        extends JpaRepository<ItemView, Long> {

    boolean existsByItemIdAndUserId(
            Long itemId,
            Long userId
    );

}
