package com.example.reserveshop.member.domain;

import com.example.reserveshop.member.vo.MemberType;
import com.example.reserveshop.member.vo.Address;
import com.example.reserveshop.member.vo.LoginId;
import com.example.reserveshop.member.vo.Password;
import com.example.reserveshop.member.vo.PhoneNumber;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "address"))
    private Address address;
    private MemberType memberType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public boolean isPartner() {
        return this.memberType.equals(MemberType.PARTNER);
    }
}
