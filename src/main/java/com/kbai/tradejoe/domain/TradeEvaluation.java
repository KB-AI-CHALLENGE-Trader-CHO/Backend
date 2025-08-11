// src/main/java/com/kbai/tradejoe/domain/TradeEvaluation.java
package com.kbai.tradejoe.domain;

import com.kbai.tradejoe.domain.embed.DailyContext;
import com.kbai.tradejoe.domain.embed.IntradayTiming;
import com.kbai.tradejoe.domain.embed.ScoreMetrics;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trade_evaluation")
public class TradeEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_history_id", nullable = false, columnDefinition = "BIGINT")
    private TradeHistory tradeHistory;

    @Embedded
    private DailyContext daily;

    @Embedded
    private IntradayTiming intra;

    @Embedded
    private ScoreMetrics score;

    @CreatedDate
    @Column(name = "evaluated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime evaluatedAt;

    @Builder
    public TradeEvaluation(TradeHistory tradeHistory, DailyContext daily, IntradayTiming intra, ScoreMetrics score) {
        this.tradeHistory = tradeHistory;
        this.daily = daily;
        this.intra = intra;
        this.score = score;
    }
}
