package com.wfms.repository;

import com.wfms.entity.Sprint;
import com.wfms.entity.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowRepository extends JpaRepository<WorkFlow,Long> {
    @Query(value = "Select * from work_flow where project_id = :projectId  ",nativeQuery = true)
    WorkFlow getDetailWorkflow(@Param("projectId") Long projectId);
}
