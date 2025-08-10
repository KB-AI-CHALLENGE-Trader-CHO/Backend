package com.kbai.tradejoe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "monthly_report")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate period;

    @Column(length = 500)
    private String summary;

    @Builder
    public MonthlyReport(LocalDate period, String summary) {
        this.period = period;
        this.summary = summary;
    }
}
