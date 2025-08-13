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

    public static AnalysisDetailsResponseDto withSignals(AnalysisDetailsResponseDto base, TradeType tradeType) {
        if (base == null) return null;
        return new AnalysisDetailsResponseDto(
                DailyAnalysis.withSignals(base.dailyContext, tradeType),
                IntradayAnalysis.withSignals(base.intradayTiming, tradeType)
        );
    }

    public record DailyAnalysis(
            Trend trend,
            MaStack maStack,
            IndicatorDetail rsi,
            IndicatorDetail stochastic,
            BandEvent bollingerEvent,
            ObvSignal obvSignal,
            AtrRegime atrRegime,
            BandEvent keltnerEvent,
            String trendSignal,
            String maStackSignal,
            String rsiSignal,
            String stochasticSignal,
            String bollingerSignal,
            String obvSignalSignal,
            String atrRegimeSignal,
            String keltnerSignal
    ) {
        static DailyAnalysis from(TradeEvaluation evaluation) {
            DailyContext d = evaluation.getDaily();
            return new DailyAnalysis(
                    d.getTrend(),
                    d.getMaStack(),
                    new IndicatorDetail(d.getRsi(), d.getRsiStatus()),
                    new IndicatorDetail(d.getStochK(), d.getStochStatus()),
                    d.getBbEvent(),
                    d.getObvSignal(),
                    d.getAtrRegime(),
                    d.getKeltnerEvent(),
                    null, null, null, null, null, null, null, null
            );
        }

        static DailyAnalysis withSignals(DailyAnalysis base, TradeType tradeType) {
            if (base == null) return null;
            boolean isBuy = tradeType == TradeType.BUY;

            String trendSig   = base.trend()          == null ? Signal.UNKNOWN.mark() : (isBuy ? base.trend().buySignal().mark()       : base.trend().sellSignal().mark());
            String maStackSig = base.maStack()        == null ? Signal.UNKNOWN.mark() : (isBuy ? base.maStack().buySignal().mark()     : base.maStack().sellSignal().mark());
            String rsiSig     = base.rsi() == null || base.rsi().status() == null
                    ? Signal.UNKNOWN.mark()
                    : (isBuy ? base.rsi().status().buySignal().mark() : base.rsi().status().sellSignal().mark());
            String stochSig   = base.stochastic() == null || base.stochastic().status() == null
                    ? Signal.UNKNOWN.mark()
                    : (isBuy ? base.stochastic().status().buySignal().mark() : base.stochastic().status().sellSignal().mark());
            String bbSig      = base.bollingerEvent() == null ? Signal.UNKNOWN.mark() : (isBuy ? base.bollingerEvent().buySignal().mark() : base.bollingerEvent().sellSignal().mark());
            String obvSig     = base.obvSignal()      == null ? Signal.UNKNOWN.mark() : (isBuy ? base.obvSignal().buySignal().mark()      : base.obvSignal().sellSignal().mark());
            String atrSig     = base.atrRegime()      == null ? Signal.UNKNOWN.mark() : (isBuy ? base.atrRegime().buySignal().mark()      : base.atrRegime().sellSignal().mark());
            String keltnerSig = base.keltnerEvent()   == null ? Signal.UNKNOWN.mark() : (isBuy ? base.keltnerEvent().buySignal().mark()   : base.keltnerEvent().sellSignal().mark());

            return new DailyAnalysis(
                    base.trend(),
                    base.maStack(),
                    base.rsi(),
                    base.stochastic(),
                    base.bollingerEvent(),
                    base.obvSignal(),
                    base.atrRegime(),
                    base.keltnerEvent(),
                    trendSig,
                    maStackSig,
                    rsiSig,
                    stochSig,
                    bbSig,
                    obvSig,
                    atrSig,
                    keltnerSig
            );
        }
    }

    public record IntradayAnalysis(
            Trend trend,
            MaStack maStack,
            IndicatorDetail rsi,
            IndicatorDetail stochastic,
            BandEvent bollingerEvent,
            Double volumeZScore,
            BandEvent keltnerEvent,
            String trendSignal,
            String maStackSignal,
            String rsiSignal,
            String stochasticSignal,
            String bollingerSignal,
            String keltnerSignal
    ) {
        static IntradayAnalysis from(TradeEvaluation evaluation) {
            IntradayTiming i = evaluation.getIntra();
            return new IntradayAnalysis(
                    i.getTrend(),
                    i.getMaStack(),
                    new IndicatorDetail(i.getRsi(), i.getStochStatus()), // 주의: 원래 코드에 rsiStatus가 아니라 stochStatus를 넣고 있었는지 확인 필요
                    new IndicatorDetail(i.getStochK(), i.getStochStatus()),
                    i.getBbEvent(),
                    i.getVolumeZ(),
                    i.getKeltnerEvent(),
                    null, null, null, null, null, null
            );
        }

        static IntradayAnalysis withSignals(IntradayAnalysis base, TradeType tradeType) {
            if (base == null) return null;
            boolean isBuy = tradeType == TradeType.BUY;

            String trendSig   = base.trend()          == null ? Signal.UNKNOWN.mark() : (isBuy ? base.trend().buySignal().mark()       : base.trend().sellSignal().mark());
            String maStackSig = base.maStack()        == null ? Signal.UNKNOWN.mark() : (isBuy ? base.maStack().buySignal().mark()     : base.maStack().sellSignal().mark());
            String rsiSig     = base.rsi() == null || base.rsi().status() == null
                    ? Signal.UNKNOWN.mark()
                    : (isBuy ? base.rsi().status().buySignal().mark() : base.rsi().status().sellSignal().mark());
            String stochSig   = base.stochastic() == null || base.stochastic().status() == null
                    ? Signal.UNKNOWN.mark()
                    : (isBuy ? base.stochastic().status().buySignal().mark() : base.stochastic().status().sellSignal().mark());
            String bbSig      = base.bollingerEvent() == null ? Signal.UNKNOWN.mark() : (isBuy ? base.bollingerEvent().buySignal().mark() : base.bollingerEvent().sellSignal().mark());
            String keltSig    = base.keltnerEvent()   == null ? Signal.UNKNOWN.mark() : (isBuy ? base.keltnerEvent().buySignal().mark()   : base.keltnerEvent().sellSignal().mark());

            return new IntradayAnalysis(
                    base.trend(),
                    base.maStack(),
                    base.rsi(),
                    base.stochastic(),
                    base.bollingerEvent(),
                    base.volumeZScore(),
                    base.keltnerEvent(),
                    trendSig,
                    maStackSig,
                    rsiSig,
                    stochSig,
                    bbSig,
                    keltSig
            );
        }
    }

    public record IndicatorDetail(Double value, Status status) {}
}