package com.kbai.tradejoe.domain;

import com.kbai.tradejoe.domain.type.TradeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "weekly_analysis")
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Lob
    @Column(name = "analysis_details")
    private String analysisDetails;

    @Lob
    private String suggestion;

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계
    @JoinColumn(name = "weekly_report_id", nullable = false)
    private WeeklyReport weeklyReport;

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계
    @JoinColumn(name = "history_id", nullable = false)
    private TradeHistory tradeHistory;

    @Builder
    public WeeklyAnalysis(LocalDateTime dateTime, TradeHistory tradeHistory, String analysisDetails, String suggestion, WeeklyReport weeklyReport) {
        this.dateTime = dateTime;
        this.tradeHistory = tradeHistory;
        this.analysisDetails = analysisDetails;
        this.suggestion = suggestion;
        this.weeklyReport = weeklyReport;
    }
}
