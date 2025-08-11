package com.kbai.tradejoe.domain.embed;

import com.kbai.tradejoe.domain.type.*;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntradayTiming {

    @Enumerated(EnumType.STRING)
    @Column(name = "intra_trend")
    private Trend trend;

    @Enumerated(EnumType.STRING)
    @Column(name = "intra_ma_stack")
    private MaStack maStack;

    @Column(name = "intra_rsi", columnDefinition = "DECIMAL(10,2)")
    private Double rsi;

    @Enumerated(EnumType.STRING)
    @Column(name = "intra_rsi_status")
    private Status rsiStatus;

    @Column(name = "intra_stoch_k", columnDefinition = "DECIMAL(10,2)")
    private Double stochK;

    @Enumerated(EnumType.STRING)
    @Column(name = "intra_stoch_status")
    private Status stochStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "intra_bb_event")
    private BandEvent bbEvent;

    @Enumerated(EnumType.STRING)
    @Column(name = "intra_keltner_event")
    private BandEvent keltnerEvent;

    @Column(name = "intra_volume_z", columnDefinition = "DECIMAL(10,2)")
    private Double volumeZ;
}
