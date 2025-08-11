package com.kbai.tradejoe.domain.embed;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreMetrics {

    @Column(name = "score_context", columnDefinition = "INT")
    private Integer context;

    @Column(name = "score_timing", columnDefinition = "INT")
    private Integer timing;

    @Column(name = "score_rationale", columnDefinition = "INT")
    private Integer rationale;

    @Column(name = "score_risk", columnDefinition = "INT")
    private Integer risk;

    @Column(name = "score_total", columnDefinition = "INT")
    private Integer total;

    @Column(name = "score_confidence", columnDefinition = "DECIMAL(4,2)")
    private Double confidence;
}
