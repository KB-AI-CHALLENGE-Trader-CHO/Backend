package com.kbai.tradejoe.service;

import com.kbai.tradejoe.domain.*;
import com.kbai.tradejoe.domain.embed.DailyContext;
import com.kbai.tradejoe.domain.embed.IntradayTiming;
import com.kbai.tradejoe.domain.embed.ScoreMetrics;
import com.kbai.tradejoe.domain.type.*;
import com.kbai.tradejoe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final TradeHistoryRepository tradeHistoryRepository;
    private final DailyMarketDataRepository dailyRepo;
    private final IntradayMarketDataRepository intraRepo;
    private final TradeEvaluationRepository evalRepo;

    @Transactional
    public void evaluateAndSave(Long tradeId) {
        TradeHistory trade = tradeHistoryRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found: " + tradeId));

        Long stockItemId = trade.getStockItem().getId();
        LocalDate tradeDate = trade.getDate();

        List<DailyMarketData> daily = dailyRepo
                .findByStockItem_IdAndDateLessThanEqualOrderByDateAsc(stockItemId, tradeDate);
        List<IntradayMarketData> intra = intraRepo
                .findByStockItem_IdAndDateOrderByIdAsc(stockItemId, tradeDate);

        DailyContext dailyContext = analyzeDailyContext(daily, tradeDate);
        IntradayTiming intradayTiming = analyzeIntradayTiming(intra);
        ScoreMetrics scores = calculateScores(dailyContext, intradayTiming);

        TradeEvaluation newTradeEval = TradeEvaluation.builder()
                .tradeHistory(trade)
                .daily(dailyContext)
                .intra(intradayTiming)
                .score(scores)
                .build();

        evalRepo.save(newTradeEval);
    }

    private DailyContext analyzeDailyContext(List<DailyMarketData> daily, LocalDate tradeDate) {
        if (daily == null || daily.isEmpty()) return DailyContext.builder().build();

        DailyMarketData latest = daily.getLast();

        MsTrend msTrend = maStack(
                latest.getClose(),
                latest.getMa20d(),
                latest.getMa50d(),
                latest.getMa100d()
        );

        AtrRegime atrRegime = percentileRegime(
                daily.stream().map(DailyMarketData::getAtr14d).filter(Objects::nonNull).toList(),
                latest.getAtr14d()
        );

        return DailyContext.builder()
                .trend(msTrend.trend())
                .maStack(msTrend.maStack())
                .rsi(latest.getRsi14d())
                .rsiStatus(rsiFlag(latest.getRsi14d()))
                .stochK(latest.getStochasticK())
                .stochStatus(stochFlag(latest.getStochasticK()))
                .bbEvent(bandEvent(latest.getClose(), latest.getBollingerUpper(), latest.getBollingerLower()))
                .atrRegime(atrRegime)
                .obvSignal(obvDivergence(daily))
                .keltnerEvent(bandEvent(latest.getClose(), latest.getKeltnerUpper(), latest.getKeltnerLower()))
                .build();
    }


    private IntradayTiming analyzeIntradayTiming(List<IntradayMarketData> intra) {
        if (intra == null || intra.isEmpty()) return IntradayTiming.builder().build();

        IntradayMarketData latest = intra.getLast();

        MsTrend msTrend = simpleMa20v50(
                latest.getClose(),
                latest.getMa20d(),
                latest.getMa50d()
        );

        Double volZ = volumeZscore(intra.stream().map(IntradayMarketData::getVolume).toList());

        return IntradayTiming.builder()
                .trend(msTrend.trend())
                .maStack(msTrend.maStack())
                .rsi(latest.getRsi14d())
                .rsiStatus(rsiFlag(latest.getRsi14d()))
                .stochK(latest.getStochasticK())
                .stochStatus(stochFlag(latest.getStochasticK()))
                .bbEvent(bandEvent(latest.getClose(), latest.getBollingerUpper(), latest.getBollingerLower()))
                .keltnerEvent(bandEvent(latest.getClose(), latest.getKeltnerUpper(), latest.getKeltnerLower()))
                .volumeZ(volZ)
                .build();
    }


    private ScoreMetrics calculateScores(DailyContext daily, IntradayTiming intra) {
        Integer context = 0;
        if (daily != null) {
            if (daily.getMaStack() == MaStack.bullish) context += 12;
            if (daily.getRsiStatus() == Status.normal) context += 8;
            if (daily.getObvSignal() == ObvSignal.bullish) context += 6;
        }
        Integer timing = 0;
        if (intra != null) {
            if (intra.getMaStack() == MaStack.bullish) timing += 15;
            if (intra.getRsiStatus() == Status.oversold) timing += 10;
            if (intra.getStochStatus() == Status.oversold) timing += 8;
            if (intra.getKeltnerEvent() == BandEvent.break_upper) timing += 10;
            Double vz = intra.getVolumeZ();
            if (vz != null && vz >= 1.5) timing += 5;
        }
        Integer total = context + timing;

        return ScoreMetrics.builder()
                .context(context)
                .timing(timing)
                .rationale(0)
                .risk(0)
                .total(total)
                .confidence(0.8)
                .build();
    }

    private record MsTrend(MaStack maStack, Trend trend) {}

    private static MsTrend maStack(Double c, Double m20, Double m50, Double m100) {
        if (anyNull(c, m20, m50, m100)) return new MsTrend(MaStack.mixed, Trend.sideways);
        if (c > m20 && m20 > m50 && m50 > m100) return new MsTrend(MaStack.bullish, Trend.uptrend);
        if (c < m20 && m20 < m50 && m50 < m100) return new MsTrend(MaStack.bearish, Trend.downtrend);
        return new MsTrend(MaStack.mixed, Trend.sideways);
    }

    private static MsTrend simpleMa20v50(Double c, Double m20, Double m50) {
        if (anyNull(c, m20, m50)) return new MsTrend(MaStack.mixed, Trend.sideways);
        if (c > m20 && m20 > m50) return new MsTrend(MaStack.bullish, Trend.uptrend);
        if (c < m20 && m20 < m50) return new MsTrend(MaStack.bearish, Trend.downtrend);
        return new MsTrend(MaStack.mixed, Trend.sideways);
    }

    private static Status rsiFlag(Double r) {
        if (r == null) return Status.unknown;
        if (r >= 70.0) return Status.overbought;
        if (r <= 30.0) return Status.oversold;
        return Status.normal;
    }

    private static Status stochFlag(Double k) {
        if (k == null) return Status.unknown;
        if (k >= 80.0) return Status.overbought;
        if (k <= 20.0) return Status.oversold;
        return Status.normal;
    }

    private static BandEvent bandEvent(Double close, Double upper, Double lower) {
        if (anyNull(close, upper, lower)) return BandEvent.unknown;
        if (close > upper) return BandEvent.break_upper;
        if (close < lower) return BandEvent.break_lower;
        double tol = 1e-4 * Math.max(Math.abs(upper), 1.0);
        if (Math.abs(close - upper) <= tol) return BandEvent.touch_upper;
        if (Math.abs(close - lower) <= tol) return BandEvent.touch_lower;
        return BandEvent.inside;
    }

    private static AtrRegime percentileRegime(List<Double> series, Double value) {
        List<Double> s = series.stream().filter(Objects::nonNull).sorted().toList();
        if (s.isEmpty() || value == null) return AtrRegime.unknown;
        double p33 = percentile(s, 33);
        double p66 = percentile(s, 66);
        if (value <= p33) return AtrRegime.low;
        if (value >= p66) return AtrRegime.high;
        return AtrRegime.mid;
    }

    private static double percentile(List<Double> sorted, int p) {
        if (sorted.isEmpty()) return Double.NaN;
        double rank = (p / 100.0) * (sorted.size() - 1);
        int lo = (int) Math.floor(rank);
        int hi = (int) Math.ceil(rank);
        if (lo == hi) return sorted.get(lo);
        return sorted.get(lo) + (rank - lo) * (sorted.get(hi) - sorted.get(lo));
    }

    private static ObvSignal obvDivergence(List<DailyMarketData> daily) {
        if (daily == null || daily.isEmpty()) return ObvSignal.none;
        List<DailyMarketData> sub = daily.subList(Math.max(daily.size() - Math.max(30, 10), 0), daily.size());
        List<Integer> pivH = pivotsHigh(sub, 2);
        List<Integer> pivL = pivotsLow(sub, 2);

        if (pivH.size() >= 2) {
            DailyMarketData h1 = sub.get(pivH.get(pivH.size() - 2));
            DailyMarketData h2 = sub.get(pivH.get(pivH.size() - 1));
            Double p1 = h1.getHigh(), p2 = h2.getHigh();
            Long o1 = h1.getObv(), o2 = h2.getObv();
            if (p1 != null && p2 != null && o1 != null && o2 != null && p2 > p1 && o2 < o1) {
                return ObvSignal.bearish;
            }
        }
        if (pivL.size() >= 2) {
            DailyMarketData l1 = sub.get(pivL.get(pivL.size() - 2));
            DailyMarketData l2 = sub.get(pivL.get(pivL.size() - 1));
            Double p1 = l1.getLow(), p2 = l2.getLow();
            Long o1 = l1.getObv(), o2 = l2.getObv();
            if (p1 != null && p2 != null && o1 != null && o2 != null && p2 < p1 && o2 > o1) {
                return ObvSignal.bullish;
            }
        }
        return ObvSignal.none;
    }

    /* ===================== 보조 유틸 ===================== */

    private static boolean anyNull(Object... xs) {
        for (Object x : xs) if (x == null) return true;
        return false;
    }

    private static List<Integer> pivotsHigh(List<DailyMarketData> d, int k) {
        List<Integer> r = new ArrayList<>();
        for (int i = k; i < d.size() - k; i++) {
            Double v = d.get(i).getHigh();
            if (v == null) continue;
            boolean isMax = true;
            for (int j = i - k; j <= i + k; j++) {
                if (j == i) continue;
                Double w = d.get(j).getHigh();
                if (w != null && w >= v) { isMax = false; break; }
            }
            if (isMax) r.add(i);
        }
        return r;
    }

    private static List<Integer> pivotsLow(List<DailyMarketData> d, int k) {
        List<Integer> r = new ArrayList<>();
        for (int i = k; i < d.size() - k; i++) {
            Double v = d.get(i).getLow();
            if (v == null) continue;
            boolean isMin = true;
            for (int j = i - k; j <= i + k; j++) {
                if (j == i) continue;
                Double w = d.get(j).getLow();
                if (w != null && w <= v) { isMin = false; break; }
            }
            if (isMin) r.add(i);
        }
        return r;
    }

    private static Double volumeZscore(List<Long> vols) {
        List<Long> v = vols.stream().filter(Objects::nonNull).toList();
        if (v.size() < 5) return null;
        double last = v.get(v.size() - 1);
        List<Long> base = v.subList(0, v.size() - 1);
        double mean = base.stream().mapToDouble(Long::doubleValue).average().orElse(Double.NaN);
        double var = base.stream().mapToDouble(x -> Math.pow(x - mean, 2)).sum() / (base.size() - 1);
        double sd = Math.sqrt(var);
        if (sd == 0 || Double.isNaN(sd)) return 0.0;
        return (last - mean) / sd;
    }
}