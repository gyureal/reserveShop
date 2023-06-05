package com.example.reserveshop.reservation.web;

import com.example.reserveshop.reservation.domain.service.ReservationService;
import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import com.example.reserveshop.reservation.web.dto.CreateReservationRequest;
import com.example.reserveshop.reservation.web.dto.ReservationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.reserveshop.reservation.domain.vo.AcceptType.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    /**
     * 예약을 생성합니다.
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ReservationInfo> createReservation(@RequestBody CreateReservationRequest request) {
        ReservationInfo reservationInfo = ReservationInfo.fromEntity(reservationService.createReservation(request));
        return ResponseEntity.created(URI.create("/reservations" + reservationInfo.getId()))
                .body(reservationInfo);
    }

    /**
     * id 값으로 예약 정보를 조회합니다.
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationInfo> searchReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationInfo.fromEntity(reservationService.getReservationById(id)));
    }

    /**
     * 예약 정보를 조회합니다.
     * 상점 id, 예약상태, 날짜범위 에 해당하는 예약을 조회합니다.
     * @param storeId
     * @param status
     * @param from
     * @param to
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ReservationInfo>> searchReservations(@RequestParam Long storeId,
                                                                    @RequestParam ReserveStatus status,
                                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> from,
                                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> to) {
        return ResponseEntity.ok(
                reservationService.getReservations(storeId, status, from, to)
                        .stream().map(ReservationInfo::fromEntity)
                        .collect(Collectors.toList()));
    }

    /**
     * 예약 승인 처리합니다.
     * @param id
     * @return
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveReservation(@PathVariable Long id) {
        reservationService.approveOrRejectReservation(id, APPROVE);
        return ResponseEntity.ok().build();
    }

    /**
     * 예약 거절 처리합니다.
     * @param id
     * @return
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectReservation(@PathVariable Long id) {
        reservationService.approveOrRejectReservation(id, REJECT);
        return ResponseEntity.ok().build();
    }

    /**
     * 예약을 방문처리 합니다.
     * @param id
     * @return
     */
    @PostMapping("/{id}/visit")
    public ResponseEntity<Void> visitReservation(@PathVariable Long id) {
        reservationService.visitReservation(id);
        return ResponseEntity.ok().build();
    }
}
