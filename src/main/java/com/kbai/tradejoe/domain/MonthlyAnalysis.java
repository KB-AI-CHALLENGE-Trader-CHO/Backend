package com.kbai.tradejoe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "monthly_analysis")
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAnalysis {

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
    @JoinColumn(name = "monthly_report_id", nullable = false)
    private MonthlyReport monthlyReport;

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계
    @JoinColumn(name = "stock_item_id", nullable = false)
    private StockItem stockItem;

    @Builder
    public MonthlyAnalysis(LocalDateTime dateTime, StockItem stockItem, String analysisDetails, String suggestion, MonthlyReport monthlyReport) {
        this.dateTime = dateTime;
        this.stockItem = stockItem;
        this.analysisDetails = analysisDetails;
        this.suggestion = suggestion;
        this.monthlyReport = monthlyReport;
    }
}
