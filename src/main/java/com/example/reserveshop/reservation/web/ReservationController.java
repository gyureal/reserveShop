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

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationInfo> createReservation(@RequestBody CreateReservationRequest request) {
        ReservationInfo reservationInfo = ReservationInfo.fromEntity(reservationService.createReservation(request));
        return ResponseEntity.created(URI.create("/reservations" + reservationInfo.getId()))
                .body(reservationInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationInfo> searchReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationInfo.fromEntity(reservationService.getReservationById(id)));
    }

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
}
