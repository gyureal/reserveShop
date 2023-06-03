package com.example.reserveshop.store.domain.dto;

import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.store.domain.entity.Store;
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

    public static StoreInfo fromEntity(Store store) {
        return StoreInfo.builder()
                .id(store.getId())
                .name(store.getName())
                .admin(MemberInfo.fromEntity(store.getAdmin()))
                .description(store.getDescription())
                .image(store.getDescription())
                .phoneNumber(store.getPhoneNumber().getValue())
                .build();
    }
}
