package com.example.reserveshop.reservation.domain.entity;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import com.example.reserveshop.store.domain.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.example.reserveshop.reservation.domain.vo.ReserveStatus.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {
    private static final String ALREADY_APPROVED = "이미 예약 승인 된 예약입니다.";
    private static final String ALREADY_REJECTED = "이미 예약 거절 된 예약입니다.";
    private static final String STATUS_MUST_REQUEST = "예약 상태가 요청(APPROVE)이어야 합니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Store store;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone_number"))
    private PhoneNumber phoneNumber;
    @Enumerated(EnumType.STRING)
    private ReserveStatus status;
    private LocalDateTime reserveDateTime;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public boolean isStatus(ReserveStatus status) {
        return this.status.equals(status);
    }

    public void approve() {
        validateApproveOrReject();
        this.status = APPROVED;
    }

    public void reject() {
        validateApproveOrReject();
        this.status = REJECTED;
    }

    private void validateApproveOrReject() {
        if (isStatus(APPROVED)) {
            throw new IllegalArgumentException(ALREADY_APPROVED);
        }
        if (isStatus(REJECTED)) {
            throw new IllegalArgumentException(ALREADY_REJECTED);
        }
        if (!isStatus(REQUEST)) {
            throw new IllegalArgumentException(STATUS_MUST_REQUEST);
        }
    }

}
