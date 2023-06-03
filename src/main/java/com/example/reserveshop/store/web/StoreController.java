package com.example.reserveshop.store.web;

import com.example.reserveshop.store.domain.dto.StoreInfo;
import com.example.reserveshop.store.domain.entity.Store;
import com.example.reserveshop.store.domain.service.StoreService;
import com.example.reserveshop.store.domain.vo.SortType;
import com.example.reserveshop.store.web.dto.CreateStoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping
    public ResponseEntity<List<StoreInfo>> searchStore(@RequestParam String storeName,
                                                       @RequestParam Optional<SortType> sortType) {
        return ResponseEntity.ok(storeService.getStore(storeName, sortType).stream()
                .map(StoreInfo::fromEntity)
                .collect(Collectors.toList()));
    }

}
