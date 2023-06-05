package com.example.reserveshop.reservation.domain.entity;

import com.example.reserveshop.reservation.domain.vo.StarRate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @ManyToOne
    private Reservation reservation;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "star_rate"))
    private StarRate starRate;
    private String title;
    private String comment;
    private LocalDateTime reviewDateTime;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
