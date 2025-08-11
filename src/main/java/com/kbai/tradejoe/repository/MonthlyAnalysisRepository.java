package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.MonthlyAnalysis;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyAnalysisRepository extends JpaRepository<MonthlyAnalysis, Long> {
    @EntityGraph(attributePaths = {"stockItem"})
    List<MonthlyAnalysis> findAllByMonthlyReportId(Long monthlyReportId);
}