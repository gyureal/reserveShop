package com.example.reserveshop.member.domain;

import com.example.reserveshop.global.constant.MemberType;
import com.example.reserveshop.member.vo.LoginId;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "loginId"))
    private LoginId loginId;
    private String password;
    private String name;
    private String phoneNumber;
    private String location;
    private MemberType memberType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
