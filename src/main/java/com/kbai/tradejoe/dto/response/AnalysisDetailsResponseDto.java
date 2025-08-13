package com.kbai.tradejoe.dto.response;

import com.kbai.tradejoe.domain.TradeEvaluation;
import com.kbai.tradejoe.domain.embed.DailyContext;
import com.kbai.tradejoe.domain.embed.IntradayTiming;

public record AnalysisDetailsResponseDto(
        DailyAnalysis dailyContext,
        IntradayAnalysis intradayTiming
) {
    public static AnalysisDetailsResponseDto from(TradeEvaluation evaluation) {
        return new AnalysisDetailsResponseDto(
                DailyAnalysis.from(evaluation),
                IntradayAnalysis.from(evaluation)
        );
    }
}

// 일봉 분석 데이터
record DailyAnalysis(
        String trend,
        String maStack,
        IndicatorDetail rsi,
        IndicatorDetail stochastic,
        String bollingerEvent,
        String obvSignal,
        String atrRegime,
        String keltnerEvent
) {
    static DailyAnalysis from(TradeEvaluation evaluation) {
        DailyContext daily = evaluation.getDaily();
        return new DailyAnalysis(
                daily.getTrend().name(),
                daily.getMaStack().name(),
                new IndicatorDetail(daily.getRsi(), daily.getRsiStatus().name()), // [수정] 제네릭 제거
                new IndicatorDetail(daily.getStochK(), daily.getStochStatus().name()), // [수정] 제네릭 제거
                daily.getBbEvent().name(),
                daily.getObvSignal().name(),
                daily.getAtrRegime().name(),
                daily.getKeltnerEvent().name()
        );
    }
}

// 분봉 분석 데이터
record IntradayAnalysis(
        String trend,
        String maStack,
        IndicatorDetail rsi,
        IndicatorDetail stochastic,
        String bollingerEvent,
        Double volumeZScore,
        String keltnerEvent
) {
    static IntradayAnalysis from(TradeEvaluation evaluation) {
        IntradayTiming intra = evaluation.getIntra();
        return new IntradayAnalysis(
                intra.getTrend().name(),
                intra.getMaStack().name(),
                new IndicatorDetail(intra.getRsi(), intra.getRsiStatus().name()),
                new IndicatorDetail(intra.getStochK(), intra.getStochStatus().name()),
                intra.getBbEvent().name(),
                intra.getVolumeZ(),
                intra.getKeltnerEvent().name()
        );
    }
}

record IndicatorDetail(Double value, String status) {}