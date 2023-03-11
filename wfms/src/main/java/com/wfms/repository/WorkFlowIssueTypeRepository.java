package com.wfms.repository;

import com.wfms.entity.WorkFlowIssueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowIssueTypeRepository extends JpaRepository<WorkFlowIssueType,Long> {
    @Query(value = "SELECT  * from work_flow_issue_type where issue_type_id= :issueTypeId and work_flow_id = :workFlowId", nativeQuery = true)
    WorkFlowIssueType getDetailWorkFlowIssueType(@Param("issueTypeId") Long issueTypeId,@Param("workFlowId") Long workFlowId);
}
