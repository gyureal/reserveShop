package com.example.reserveshop.store.domain.entity;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.vo.MemberType;
import com.example.reserveshop.member.vo.Address;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.store.domain.vo.Image;
import com.example.reserveshop.store.domain.vo.StoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Store {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Member admin;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "address"))
    private Address address;
    private String description;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "image"))
    private Image image;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone_number"))
    private PhoneNumber phoneNumber;
    @Enumerated(value = EnumType.STRING)
    private StoreType storeType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
