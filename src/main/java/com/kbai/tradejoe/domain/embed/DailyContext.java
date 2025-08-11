package com.kbai.tradejoe.domain.embed;

import com.kbai.tradejoe.domain.type.*;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyContext {

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_trend")
    private Trend trend;

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_ma_stack")
    private MaStack maStack;

    @Column(name = "daily_rsi", columnDefinition = "DECIMAL(10,2)")
    private Double rsi;

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_rsi_status")
    private Status rsiStatus;

    @Column(name = "daily_stoch_k", columnDefinition = "DECIMAL(10,2)")
    private Double stochK;

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_stoch_status")
    private Status stochStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_bb_event")
    private BandEvent bbEvent;

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_atr_regime")
    private AtrRegime atrRegime;

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_obv_signal")
    private ObvSignal obvSignal;

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_keltner_event")
    private BandEvent keltnerEvent;

}
