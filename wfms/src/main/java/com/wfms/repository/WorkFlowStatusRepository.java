package com.wfms.repository;

import com.wfms.entity.WorkFlowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowStatusRepository extends JpaRepository<WorkFlowStatus,Long> {
}
