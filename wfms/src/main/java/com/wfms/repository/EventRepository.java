package com.wfms.repository;

import com.wfms.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    @Query(value = "select * from event where start_date = :startTime and status = 1 order by create_date desc limit 1",nativeQuery = true)
    Event findEventWithStartTime(@Param("startTime") LocalDateTime startTime);

    @Modifying
    @Transactional
    @Query(value = "Update event set status = 0 where status = 1 and end_date < :endDate ",nativeQuery = true)
    void updateStatusEvent(@Param("endDate")LocalDateTime endDate);

    @Query(value = "select * from event where status = 1 and date_part('hour',cast(start_date as TIMESTAMP)) - date_part('hour', cast(:startTime as TIMESTAMP)) = 1",nativeQuery = true)
    List<Event> findEventWithMeetingInOneHour(@Param("startTime") LocalDateTime startTime);

    @Query(value = "Select * from event  where  " +
            " (:projectId is null OR (project_id)= :projectId)"+
            "and  (cast(:date as date) is null  OR (start_date) > :date )" +
            "and  (cast(:toDate as date)  is null OR (start_date) < :toDate )" +
            " and (:status is null OR (status)= :status)",nativeQuery = true)
    Page<Event> searchEvent(@Param("projectId") Long projectId, @Param("status")Integer status,@Param("date")LocalDateTime date,@Param("toDate")LocalDateTime toDate, Pageable pageable);
}
