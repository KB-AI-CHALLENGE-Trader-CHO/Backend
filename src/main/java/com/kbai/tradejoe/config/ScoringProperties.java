package com.kbai.tradejoe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;

@ConfigurationProperties(prefix = "scoring")
public record ScoringProperties(
        String activeRubric,
        Map<String, Weights> rubrics
) {
    public record Weights(Context context, Timing timing) {}

    public record Context(
            Integer maStack,
            Integer rsiNormal,
            Integer obvDivergence,
            Integer atrNotHigh,
            Integer dailyStochReversal,
            Integer atrLow,
            Integer dailyRsiReversal,
            Integer dailyBbBreakout
    ) {}

    public record Timing(
            Integer maStack,
            Integer stochReversal,
            Integer rsiReversal,
            Integer keltnerBreakout,
            Integer volumeSurge,
            Integer keltnerAndVolumeBreakout
    ) {}
}