package com.wfms.repository;

import com.wfms.entity.WorkFlowIssueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowIssueTypeRepository extends JpaRepository<WorkFlowIssueType,Long> {
}
