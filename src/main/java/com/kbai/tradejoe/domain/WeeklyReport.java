package com.kbai.tradejoe.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "weekly_report")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate period;

    @Column(length = 500)
    private String summary;

    @Builder
    public WeeklyReport(LocalDate period, String summary) {
        this.period = period;
        this.summary = summary;
    }
}
