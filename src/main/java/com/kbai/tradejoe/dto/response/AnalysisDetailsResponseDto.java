package com.kbai.tradejoe.dto.response; // 패키지 경로는 맞게 수정해주세요.

import com.kbai.tradejoe.domain.TradeEvaluation;

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
        IndicatorDetail<Double> rsi,
        IndicatorDetail<Double> stochastic,
        String bollingerEvent,
        String obvSignal,
        String atrRegime,
        String keltnerEvent
) {
    static DailyAnalysis from(TradeEvaluation evaluation) {
        var daily = evaluation.getDaily();
        return new DailyAnalysis(
                daily.getTrend().name(),
                daily.getMaStack().name(),
                new IndicatorDetail<>(daily.getRsi(), daily.getRsiStatus().name()),
                new IndicatorDetail<>(daily.getStochK(), daily.getStochStatus().name()),
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
        IndicatorDetail<Double> rsi,
        IndicatorDetail<Double> stochastic,
        String bollingerEvent,
        Double volumeZScore,
        String keltnerEvent
) {
    static IntradayAnalysis from(TradeEvaluation evaluation) {
        var intra = evaluation.getIntra();
        return new IntradayAnalysis(
                intra.getTrend().name(),
                intra.getMaStack().name(),
                new IndicatorDetail<>(intra.getRsi(), intra.getRsiStatus().name()),
                new IndicatorDetail<>(intra.getStochK(), intra.getStochStatus().name()),
                intra.getBbEvent().name(),
                intra.getVolumeZ(),
                intra.getKeltnerEvent().name()
        );
    }
}

// 지표 값과 상태를 함께 담는 제네릭 레코드
record IndicatorDetail<T>(T value, String status) {}