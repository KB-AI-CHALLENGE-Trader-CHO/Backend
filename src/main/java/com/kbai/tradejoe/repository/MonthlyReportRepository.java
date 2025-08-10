package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {
}
