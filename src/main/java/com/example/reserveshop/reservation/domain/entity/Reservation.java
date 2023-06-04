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
    private static final String ALREADY_VISIT = "이미 방문처리 되었습니다.";
    private static final String STATUS_MUST_APPROVED = "승인 상태의 예약만 방문 처리 가능합니다.";
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

    /**
     * 예약을 승인 처리를 합니다.
     * @throws IllegalStateException 예약 상태가 요청(APPROVE)이 아닌 경우
     */
    public void approve() {
        validateApproveOrReject();
        this.status = APPROVED;
    }

    /**
     * 예약을 거절 처리를 합니다.
     * @throws IllegalStateException 예약 상태가 요청(APPROVE)이 아닌 경우
     */
    public void reject() {
        validateApproveOrReject();
        this.status = REJECTED;
    }

    private void validateApproveOrReject() {
        if (isStatus(APPROVED)) {
            throw new IllegalStateException(ALREADY_APPROVED);
        }
        if (isStatus(REJECTED)) {
            throw new IllegalStateException(ALREADY_REJECTED);
        }
        if (!isStatus(REQUEST)) {
            throw new IllegalStateException(STATUS_MUST_REQUEST);
        }
    }

    /**
     * 예약을 방문 처리를 합니다.
     * @throws IllegalStateException 예약 상태가 방문(VISIT)이 아닌 경우
     */
    public void visit() {
        validateVisit();
        this.status = VISIT;
    }

    private void validateVisit() {
        if (isStatus(VISIT)) {
            throw new IllegalStateException(ALREADY_VISIT);
        }
        if (!isStatus(APPROVED)) {
            throw new IllegalStateException(STATUS_MUST_APPROVED);
        }
    }

}
