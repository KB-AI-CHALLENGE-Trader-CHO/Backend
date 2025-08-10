package com.kbai.tradejoe.domain;

import com.kbai.tradejoe.domain.type.TradeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trade_history")
public class TradeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trade_date", nullable = false)
    private LocalDate date;

    @Column(name = "trade_time", nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType tradeType;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Double price;

    @Column(name = "avg_buy_price")
    private Double avgBuyPrice;

    @Column(name = "memo")
    private String memo;

    @Builder
    public TradeHistory(LocalDate date, LocalTime time, String name, String symbol, TradeType tradeType, Double quantity, Double price, Double avgBuyPrice, String memo) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.symbol = symbol;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
        this.avgBuyPrice = avgBuyPrice;
        this.memo = memo;
    }
}
