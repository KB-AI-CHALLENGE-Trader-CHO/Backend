package com.kbai.tradejoe.dto.response;

import com.kbai.tradejoe.domain.TradeEvaluation;
import com.kbai.tradejoe.domain.embed.DailyContext;
import com.kbai.tradejoe.domain.embed.IntradayTiming;
import com.kbai.tradejoe.domain.type.*;

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

    // 일봉 분석 데이터
    record DailyAnalysis(
            Trend trend,
            MaStack maStack,
            IndicatorDetail rsi,
            IndicatorDetail stochastic,
            BandEvent bollingerEvent,
            ObvSignal obvSignal,
            AtrRegime atrRegime,
            BandEvent keltnerEvent
    ) {
        static DailyAnalysis from(TradeEvaluation evaluation) {
            DailyContext daily = evaluation.getDaily();
            return new DailyAnalysis(
                    daily.getTrend(),
                    daily.getMaStack(),
                    new IndicatorDetail(daily.getRsi(), daily.getRsiStatus()),
                    new IndicatorDetail(daily.getStochK(), daily.getStochStatus()),
                    daily.getBbEvent(),
                    daily.getObvSignal(),
                    daily.getAtrRegime(),
                    daily.getKeltnerEvent()
            );
        }
    }

    // 분봉 분석 데이터
    record IntradayAnalysis(
            Trend trend,
            MaStack maStack,
            IndicatorDetail rsi,
            IndicatorDetail stochastic,
            BandEvent bollingerEvent,
            Double volumeZScore,
            BandEvent keltnerEvent
    ) {
        static IntradayAnalysis from(TradeEvaluation evaluation) {
            IntradayTiming intra = evaluation.getIntra();
            return new IntradayAnalysis(
                    intra.getTrend(),
                    intra.getMaStack(),
                    new IndicatorDetail(intra.getRsi(), intra.getRsiStatus()),
                    new IndicatorDetail(intra.getStochK(), intra.getStochStatus()),
                    intra.getBbEvent(),
                    intra.getVolumeZ(),
                    intra.getKeltnerEvent()
            );
        }
    }

    record IndicatorDetail(Double value, Status status) {}
}

