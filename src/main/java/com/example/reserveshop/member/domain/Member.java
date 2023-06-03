package com.example.reserveshop.member.domain;

import com.example.reserveshop.global.constant.MemberType;
import com.example.reserveshop.member.vo.LoginId;
import com.example.reserveshop.member.vo.Password;
import com.example.reserveshop.member.vo.PhoneNumber;
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
    @AttributeOverride(name = "value", column = @Column(name = "login_id"))
    private LoginId loginId;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private Password password;
    private String name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone_number"))
    private PhoneNumber phoneNumber;
    private String location;
    private MemberType memberType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
