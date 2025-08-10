package com.kbai.tradejoe.domain;

import com.kbai.tradejoe.domain.type.TradeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "analysis")
@NoArgsConstructor
@AllArgsConstructor
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "stock_name", nullable = false, length = 100)
    private String stockName;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false, length = 10)
    private TradeType tradeType;

    @Column(length = 255)
    private String memo;

    @Lob
    @Column(name = "analysis_details")
    private String analysisDetails;

    @Lob
    private String suggestion;

    @Builder
    public Analysis(LocalDateTime dateTime, String stockName, String symbol,
                    TradeType tradeType, String memo, String analysisDetails, String suggestion) {
        this.dateTime = dateTime;
        this.stockName = stockName;
        this.symbol = symbol;
        this.tradeType = tradeType;
        this.memo = memo;
        this.analysisDetails = analysisDetails;
        this.suggestion = suggestion;
    }
}
