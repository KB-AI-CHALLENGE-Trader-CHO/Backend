package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {
    Optional<MonthlyReport> findFirstByPeriodBetween(LocalDate startInclusive, LocalDate endInclusive);
}