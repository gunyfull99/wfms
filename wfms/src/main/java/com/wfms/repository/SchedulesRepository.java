package com.wfms.repository;

import com.wfms.entity.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules,Long> {
    @Query(value = "select * from schedules where start_date = :startTime and status = 1 order by create_date desc limit 1",nativeQuery = true)
    Schedules findSchedulesWithStartTime(@Param("startTime")LocalDateTime startTime);

    @Modifying
    @Transactional
    @Query(value = "Update schedules set status = 0 where status = 1 and end_date < :endDate ",nativeQuery = true)
    void updateStatusSchedules(@Param("endDate")LocalDateTime endDate);

    @Query(value = "select * from schedules where date_part('hour',TIMESTAMP :startTime) - date_part('hour',TIMESTAMP start_date) = 1",nativeQuery = true)
    List<Schedules> findScheduleWithMeetingInOneHour(@Param("startTime")LocalDateTime startTime);
}
