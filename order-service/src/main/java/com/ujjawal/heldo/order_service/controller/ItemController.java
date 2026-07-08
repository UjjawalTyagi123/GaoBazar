package com.ujjawal.heldo.order_service.controller;

import com.ujjawal.heldo.order_service.dto.MyItemResponse;
import com.ujjawal.heldo.order_service.entity.Item;
import com.ujjawal.heldo.order_service.exception.InappropriateImageException;
import com.ujjawal.heldo.order_service.service.ItemService;
import com.ujjawal.heldo.order_service.service.JwtService;
import com.ujjawal.heldo.order_service.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@CrossOrigin
@RequiredArgsConstructor
public class ItemController {

    private static final Logger log =
            LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;
    private final RateLimiterService rateLimiterService;
    private final JwtService jwtService;

    /**
     * Create new item
     */
    @PostMapping
    public ResponseEntity<?> postItem(

            @RequestHeader("Authorization")
            String authorization,

            @RequestHeader("X-Device-Id")
            String deviceId,

            @RequestBody
            Item item) {

        try {

            Long userId =
                    jwtService.extractUserId(
                            authorization.replace("Bearer ", ""));

            log.info(
                    "POST /items called | userId={} | deviceId={} | villageId={}",
                    userId,
                    deviceId,
                    item.getVillageId());

            rateLimiterService.validatePost(deviceId);

            Item saved =
                    itemService.save(userId, item);

            log.info(
                    "Item saved successfully | itemId={} | userId={}",
                    saved.getId(),
                    userId);

            return ResponseEntity.ok(saved);

        } catch (InappropriateImageException ex) {

            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }

    /**
     * Marketplace items
     */
    @GetMapping
    public ResponseEntity<Page<Item>> getItems(

            @RequestParam
            Long villageId,
            @RequestParam
            Long districtId,
            @RequestParam
            String category,
            @RequestParam String scope,
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        log.info(
                "GET /items | villageId={} | category={} | page={} | size={}",
                villageId,
                category,
                page,
                size);

        Page<Item> result =
                itemService.getByVillage(
                        villageId,
                        districtId,
                        category,
                        scope,
                        page,
                        size);

        return ResponseEntity.ok(result);
    }

    /**
     * Logged-in user's items
     */
    @GetMapping("/my-items")
    public Page<MyItemResponse> getMyItems(

            @RequestHeader("Authorization")
            String authorization,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Long userId =
                jwtService.extractUserId(
                        authorization.replace("Bearer ", ""));

        return itemService.getMyItems(
                userId,
                page,
                size);
    }

    /**
     * Soft delete item
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(

            @RequestHeader("Authorization")
            String authorization,

            @PathVariable
            Long itemId) {

        Long userId =
                jwtService.extractUserId(
                        authorization.replace("Bearer ", ""));

        itemService.deleteItem(
                userId,
                itemId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Mark item as sold
     */
    @PatchMapping("/{itemId}/sold")
    public ResponseEntity<Void> markAsSold(

            @RequestHeader("Authorization")
            String authorization,

            @PathVariable
            Long itemId) {

        Long userId =
                jwtService.extractUserId(
                        authorization.replace("Bearer ", ""));

        itemService.markAsSold(
                userId,
                itemId);

        return ResponseEntity.noContent().build();
    }

}