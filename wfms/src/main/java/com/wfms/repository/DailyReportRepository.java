package com.wfms.repository;

import com.wfms.entity.DailyReport;
import com.wfms.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport,Long> {
    @Query(value = "SELECT * FROM daily_report where project_id = :projectId",nativeQuery = true)
    List<DailyReport> findByProjectId(@Param("projectId") Long projectId);

    @Query(value = "SELECT * FROM daily_report where project_id = :projectId and member_do_work = :userId " +
            "order by create_date desc limit 1 ",nativeQuery = true)
    DailyReport getLastDailyOfUser(@Param("projectId") Long projectId,@Param("userId") Long userId);

    @Query(value = "Select i from DailyReport i where  " +
            " (:projectId is null OR (i.projects.projectId)= :projectId)" +
            "and (:status is null OR (i.status) = :status) " +
            "and (:taskId is null OR (i.taskId) = :taskId) " +
            "and (:userId is null OR (i.memberDoWork) = :userId) " +
            "and  (cast(:date as text)  is null OR cast(i.createDate as text) like %:date% ) ")
    Page<DailyReport> searchDailyReport(@Param("projectId") Long projectId,
                                        @Param("userId") Long userId,
                                        @Param("date")String date,
                                        @Param("taskId")Long taskId,
                                        @Param("status") Integer status, Pageable pageable);
}
