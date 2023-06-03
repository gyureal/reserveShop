package com.example.reserveshop.store.web;

import com.example.reserveshop.store.domain.dto.StoreInfo;
import com.example.reserveshop.store.domain.service.StoreService;
import com.example.reserveshop.store.web.dto.CreateStoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreInfo> createStore(@RequestBody CreateStoreRequest request) {
        StoreInfo storeInfo =  StoreInfo.fromEntity(storeService.joinStore(request));
        return ResponseEntity.created(URI.create("/stores/" + storeInfo))
                .body(storeInfo);
    }

}
