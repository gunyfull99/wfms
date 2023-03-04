package com.wfms.repository;

import com.wfms.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport,Long> {
    @Query(value = "SELECT * FROM daily_report where project_id = :projectId",nativeQuery = true)
    List<DailyReport> findByProjectId(@Param("projectId") Long projectId);
}
