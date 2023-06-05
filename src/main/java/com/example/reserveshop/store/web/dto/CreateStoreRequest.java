package com.example.reserveshop.store.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateStoreRequest {
    private String storeName;
    private Long adminMemberId;
    private String address;
    private String description;
    private String image;
    private String phoneNumber;
}
