package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
    Optional<WeeklyReport> findFirstByPeriodBetween(LocalDate startInclusive, LocalDate endInclusive);
}
