package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
}
