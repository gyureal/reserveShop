package com.example.reserveshop.reservation.web;

import com.example.reserveshop.reservation.domain.service.ReservationService;
import com.example.reserveshop.reservation.web.dto.CreateReservationRequest;
import com.example.reserveshop.reservation.web.dto.ReservationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
}
