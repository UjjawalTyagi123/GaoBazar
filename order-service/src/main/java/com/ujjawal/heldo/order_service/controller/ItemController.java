package com.ujjawal.heldo.order_service.controller;

import com.ujjawal.heldo.order_service.entity.Item;
import com.ujjawal.heldo.order_service.service.ItemService;
import com.ujjawal.heldo.order_service.service.RateLimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@CrossOrigin
public class ItemController {

    private static final Logger log =
            LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;
    private final RateLimiterService rateLimiterService;

    public ItemController(ItemService itemService,
                          RateLimiterService rateLimiterService) {
        this.itemService = itemService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping
    public ResponseEntity<?> postItem(
            @RequestHeader("X-Device-Id") String deviceId,
            @RequestBody Item item) {

        log.info("POST /items called | deviceId={} | villageId={}",
                deviceId, item.getVillageId());

        rateLimiterService.validatePost(deviceId);

        Item saved = itemService.save(item);

        log.info("Item saved successfully | itemId={} | deviceId={}",
                saved.getId(), deviceId);

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<Page<Item>> getItems(
            @RequestParam Long villageId,
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /items called | villageId={} | category={} | page={} | size={}",
                villageId,category, page, size);

        Page<Item> result = itemService.getByVillage(villageId,category, page, size);

        log.info("Items fetched | villageId={} | totalElements={}",
                villageId, result.getTotalElements());

        return ResponseEntity.ok(result);
    }
}