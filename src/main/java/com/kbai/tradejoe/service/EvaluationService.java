package com.kbai.tradejoe.service;

import com.kbai.tradejoe.domain.*;
import com.kbai.tradejoe.domain.embed.DailyContext;
import com.kbai.tradejoe.domain.embed.IntradayTiming;
import com.kbai.tradejoe.domain.embed.ScoreMetrics;
import com.kbai.tradejoe.domain.type.*;
import com.kbai.tradejoe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final DailyMarketDataRepository dailyRepo;
    private final IntradayMarketDataRepository intraRepo;
    private final TradeEvaluationRepository evalRepo;

    private static final LocalTime TRADE_START = LocalTime.of(20, 30);
    private static final LocalTime TRADE_END = LocalTime.of(3, 0);

    @Transactional
    public void evaluateAndSave(TradeHistory trade) { // 비교 완료
        LocalDateTime startDate = computeStartDateTime(trade.getDate(), trade.getTime());

        List<DailyMarketData> daily = dailyRepo
                .findRecent100(trade.getStockItem(), trade.getDate(), PageRequest.of(0, 100));
        daily.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        List<IntradayMarketData> intra = intraRepo
                .findInRange(trade.getStockItem(), startDate, LocalDateTime.of(trade.getDate(), trade.getTime()));

        DailyContext dailyContext = analyzeDailyContext(daily);
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

    private DailyContext analyzeDailyContext(List<DailyMarketData> daily) { // 확인
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


    private IntradayTiming analyzeIntradayTiming(List<IntradayMarketData> intra) { // 확인 완
        if (intra == null || intra.isEmpty()) return IntradayTiming.builder().build();

        IntradayMarketData latest = intra.getLast();

        MsTrend msTrend = simpleMa12v20(
                latest.getClose(),
                latest.getMa12p(), //12
                latest.getMa20p() // 20
        );

        Double volZ = volumeZscore(intra.stream().map(IntradayMarketData::getVolume).toList());

        return IntradayTiming.builder()
                .trend(msTrend.trend())
                .maStack(msTrend.maStack())
                .rsi(latest.getRsi14p())
                .rsiStatus(rsiFlag(latest.getRsi14p()))
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

    /* =====================  계산 메소드 ===================== */

    private record MsTrend(MaStack maStack, Trend trend) {}

    private static MsTrend maStack(Double c, Double m20, Double m50, Double m100) {
        if (anyNull(c, m20, m50, m100)) return new MsTrend(MaStack.mixed, Trend.sideways);
        if (c > m20 && m20 > m50 && m50 > m100) return new MsTrend(MaStack.bullish, Trend.uptrend);
        if (c < m20 && m20 < m50 && m50 < m100) return new MsTrend(MaStack.bearish, Trend.downtrend);
        return new MsTrend(MaStack.mixed, Trend.sideways);
    }

    private static MsTrend simpleMa12v20(Double c, Double m12, Double m20) {
        if (anyNull(c, m12, m20)) return new MsTrend(MaStack.mixed, Trend.sideways);
        if (c > m12 && m12 > m20) return new MsTrend(MaStack.bullish, Trend.uptrend);
        if (c < m12 && m12 < m20) return new MsTrend(MaStack.bearish, Trend.downtrend);
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

    private static double percentile(List<Double> sorted, int p) { // 수정해야될듯
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

    private static LocalDateTime computeStartDateTime(LocalDate d,  LocalTime t) {
        DayOfWeek dow = d.getDayOfWeek();

        LocalDateTime from;

        if (dow == DayOfWeek.MONDAY) { // 성진 주석보고 확인할 것
            // 월 20:30~23:59 -> 금요일 20:30부터
            from = LocalDateTime.of(d.minusDays(3), TRADE_START);
        } else if (dow == DayOfWeek.TUESDAY && t.isBefore(TRADE_END)) {
            // 화 00:00~3:00 -> 금요일 20:30부터
            from = LocalDateTime.of(d.minusDays(4), TRADE_START);
        } else if (t.equals(TRADE_START) || t.isAfter(TRADE_START)) {
            // 평일 20:30~23:59 -> 전날 20:30부터
            from = LocalDateTime.of(d.minusDays(1), TRADE_START);
        } else {
            // 평일 00:00~03:00 -> 이틀 전 20:30부터
            from = LocalDateTime.of(d.minusDays(2), TRADE_START);
        }

        return from;
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
                if (w != null && w < v) { isMin = false; break; } // = 은 뺌
            }
            if (isMin) r.add(i);
        }
        return r;
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
                if (w != null && w > v) { isMax = false; break; } // = 은 뺌
            }
            if (isMax) r.add(i);
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

    /* ===================== 보조 유틸 ===================== */

    private static boolean anyNull(Object... xs) {
        for (Object x : xs) if (x == null) return true;
        return false;
    }

}