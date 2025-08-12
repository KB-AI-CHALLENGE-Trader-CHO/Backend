package com.kbai.tradejoe.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "intraday_market_data")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntradayMarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_item_id", nullable = false)
    private StockItem stockItem;

    @Column(name = "intra_date_time", nullable = false)
    private LocalDateTime date;

    @Column(name = "open_price")
    private Double open;

    @Column(name = "high_price")
    private Double high;

    @Column(name = "low_price")
    private Double low;

    @Column(name = "close_price")
    private Double close;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "ma_20_period")
    private Double ma20p;

    @Column(name = "ma_12_period")
    private Double ma12p;

    @Column(name = "rsi_14_period")
    private Double rsi14p;

    @Column(name = "bollinger_mid")
    private Double bollingerMid;

    @Column(name = "bollinger_upper")
    private Double bollingerUpper;

    @Column(name = "bollinger_lower")
    private Double bollingerLower;

    @Column(name = "atr_14_period")
    private Double atr14Period;

    @Column(name = "stochastic_k")
    private Double stochasticK;

    @Column(name = "stochastic_d")
    private Double stochasticD;

    @Column(name = "obv")
    private Long obv;

    @Column(name = "keltner_mid")
    private Double keltnerMid;

    @Column(name = "keltner_upper")
    private Double keltnerUpper;

    @Column(name = "keltner_lower")
    private Double keltnerLower;
}
