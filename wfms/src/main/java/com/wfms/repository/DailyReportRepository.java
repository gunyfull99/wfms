package com.wfms.repository;

import com.wfms.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport,Long> {
    List<DailyReport> findByProjectId(Long projectId);
}
