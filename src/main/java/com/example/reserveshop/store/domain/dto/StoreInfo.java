package com.example.reserveshop.store.domain.dto;

import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.member.vo.Address;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.store.domain.entity.Store;
import com.example.reserveshop.store.domain.vo.Image;
import com.example.reserveshop.store.domain.vo.StoreType;
import com.example.reserveshop.store.web.dto.CreateStoreRequest;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StoreInfo {

    private Long id;
    private String name;
    private MemberInfo admin;
    private String description;
    private String image;
    private String phoneNumber;
    private StoreType storeType;

    public static StoreInfo fromEntity(Store store) {
        return StoreInfo.builder()
                .id(store.getId())
                .name(store.getName())
                .admin(MemberInfo.fromEntity(store.getAdmin()))
                .description(store.getDescription())
                .image(store.getDescription())
                .phoneNumber(store.getPhoneNumber().getValue())
                .storeType(store.getStoreType())
                .build();
    }
}
