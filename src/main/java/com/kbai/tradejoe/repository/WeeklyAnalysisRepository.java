package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.WeeklyAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyAnalysisRepository extends JpaRepository<WeeklyAnalysis, Long> {
    List<WeeklyAnalysis> findAllByWeeklyReportId(Long weeklyReportId);
}
