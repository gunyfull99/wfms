package com.wfms.repository;

import com.wfms.entity.WorkFlowTaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowTaskTypeRepository extends JpaRepository<WorkFlowTaskType,Long> {
    @Query(value = "SELECT  * from work_flow_task_type where task_type_id= :taskTypeId and work_flow_id = :workFlowId", nativeQuery = true)
    WorkFlowTaskType getDetailWorkFlowTaskType(@Param("taskTypeId") Long taskTypeId, @Param("workFlowId") Long workFlowId);
}
