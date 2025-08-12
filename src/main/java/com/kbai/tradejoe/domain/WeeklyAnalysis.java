package com.kbai.tradejoe.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "weekly_analysis")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WeeklyAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "analysis_details", columnDefinition = "LONGTEXT")
    private String analysisDetails;

    @CreatedDate // Auditing 기능이 이 필드를 자동으로 채워줍니다.
    @Column(name = "date_time", nullable = false, updatable = false, columnDefinition = "datetime")
    private LocalDate date;

    @Lob
    private String suggestion;

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계
    @JoinColumn(name = "weekly_report_id")
    private WeeklyReport weeklyReport;

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계
    @JoinColumn(name = "history_id", nullable = false)
    private TradeHistory tradeHistory;

    @Builder
    public WeeklyAnalysis(TradeHistory tradeHistory, String analysisDetails, String suggestion, WeeklyReport weeklyReport) {
        this.tradeHistory = tradeHistory;
        this.analysisDetails = analysisDetails;
        this.suggestion = suggestion;
        this.weeklyReport = weeklyReport;
    }
}
