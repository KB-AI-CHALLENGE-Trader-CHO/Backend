package com.kbai.tradejoe.domain;

import com.kbai.tradejoe.domain.type.TradeType;
import com.kbai.tradejoe.dto.request.TradeRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "trade_history")
public class TradeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trade_date", nullable = false)
    private LocalDate date;

    @Column(name = "trade_time", nullable = false)
    private LocalTime time;

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

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계
    @JoinColumn(name = "stock_item_id", nullable = false)
    private StockItem stockItem;

    @Builder
    public TradeHistory(LocalDate date, LocalTime time, StockItem stockItem, TradeType tradeType, Double quantity, Double price, Double avgBuyPrice, String memo) {
        this.date = date;
        this.time = time;
        this.stockItem = stockItem;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
        this.avgBuyPrice = avgBuyPrice;
        this.memo = memo;
    }

    public void addStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public void updateTradeDetails(TradeRequestDto entity) {
        this.date = entity.date();
        this.time = entity.time();
        this.tradeType = entity.type();
        this.quantity = entity.quantity();
        this.price = entity.price();
        this.avgBuyPrice = entity.avgBuyPrice();
        this.memo = entity.memo();
    }
}
