package com.ujjawal.heldo.order_service.service;

import com.ujjawal.heldo.order_service.entity.Item;
import com.ujjawal.heldo.order_service.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private static final Logger log =
            LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository repository;
    private final ImageModerationService moderationService;

    public ItemService(ItemRepository repository,ImageModerationService moderationService) {
        this.repository = repository;
        this.moderationService = moderationService;
    }

    public Item save(Item item) {
        log.info("Saving item | villageId={} | title={}",
                item.getVillageId(), item.getTitle());
        moderationService.validate(
                item.getImageUrl()
        );
        return repository.save(item);
    }

    public Page<Item> getByVillage(Long villageId,String category, int page, int size) {

        log.info("Fetching items | villageId={} | page={} | size={}",
                villageId, page, size);
        return repository.findByVillageIdAndCategory(
                villageId,category,
                PageRequest.of(page, size)
        );
    }
}