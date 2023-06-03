package com.example.reserveshop.store.domain.service;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.domain.MemberService;
import com.example.reserveshop.member.vo.Address;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.store.domain.entity.Store;
import com.example.reserveshop.store.domain.repository.StoreRepository;
import com.example.reserveshop.store.domain.vo.Image;
import com.example.reserveshop.store.domain.vo.SortType;
import com.example.reserveshop.store.web.dto.CreateStoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private static final String JOIN_ONLY_CAN_PARTNER_MEMBER = "매장 등록은 파트너 회원만 가능합니다.";
    private static final String STORE_NOT_FOUND = "매장 정보를 찾을 수 없습니다.";
    private final StoreRepository storeRepository;
    private final MemberService memberService;

    /**
     * 매장을 등록합니다.
     * 파트너 회원만 등록 가능합니다.
     * @param request
     * @return
     */
    @Transactional
    public Store joinStore(CreateStoreRequest request) {
        Member member = memberService.getMember(request.getAdminMemberId());
        if (!member.isPartner()) {
            throw new IllegalArgumentException(JOIN_ONLY_CAN_PARTNER_MEMBER);
        }
        return storeRepository.save(Store.builder()
                .name(request.getStoreName())
                .admin(member)
                .address(Address.of(request.getAddress()))
                .description(request.getDescription())
                .image(Image.of(request.getImage()))
                .phoneNumber(PhoneNumber.of(request.getPhoneNumber()))
                .build());
    }

    public List<Store> getStore(String storeName, Optional<SortType> sortType) {
        List<Store> stores = storeRepository.findByNameStartsWithOrderByName(storeName);

        return stores;
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(STORE_NOT_FOUND));
    }
}
