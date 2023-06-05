package com.example.reserveshop.store.web;

import com.example.reserveshop.store.domain.dto.StoreInfo;
import com.example.reserveshop.store.domain.StoreService;
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

    /**
     * 상점을 등록합니다.
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<StoreInfo> createStore(@RequestBody CreateStoreRequest request) {
        StoreInfo storeInfo =  StoreInfo.fromEntity(storeService.joinStore(request));
        return ResponseEntity.created(URI.create("/stores/" + storeInfo))
                .body(storeInfo);
    }

    /**
     * 상점명으로 상점을 조회합니다.
     * sortType (가나다 순, 별점 순, 거리 순) 을 기준으로 정렬 합니다.
     * @param storeName
     * @param sortType
     * @return 상점 목록
     */
    @GetMapping
    public ResponseEntity<List<StoreInfo>> searchStore(@RequestParam String storeName,
                                                       @RequestParam Optional<SortType> sortType) {
        return ResponseEntity.ok(storeService.getStore(storeName, sortType).stream()
                .map(StoreInfo::fromEntity)
                .collect(Collectors.toList()));
    }

    /**
     * id 값으로 상점을 조회합니다.
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<StoreInfo> searchStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(StoreInfo.fromEntity(storeService.getStoreById(id)));
    }

}
