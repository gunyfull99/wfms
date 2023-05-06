package com.wfms.repository;

import com.wfms.entity.WorkFlowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkFlowStepRepository extends JpaRepository<WorkFlowStep,Long> {

    @Query(value = "SELECT * FROM work_flow_step where work_flow_id = :id order by step asc and status = 1",nativeQuery = true)
    List<WorkFlowStep> getWorkFLowStepByWorkFlowId(@Param("id") Long id);

    @Query(value = "SELECT * FROM work_flow_step where work_flow_id = :id and start = true",nativeQuery = true)
    WorkFlowStep getWorkFLowStepStart(@Param("id") Long id);
}
