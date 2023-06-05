package com.example.reserveshop.reservation.domain.entity;

import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReserveHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    /**
     * 히스토리의 상태값은 수정할 수 없습니다.
     * 상태값 변경 시, 새 히스토리를 추가하세요.
     */
    @JoinColumn(updatable = false)
    private ReserveStatus statusHistory;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
