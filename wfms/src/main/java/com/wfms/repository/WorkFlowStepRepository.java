package com.wfms.repository;

import com.wfms.entity.WorkFlowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowStepRepository extends JpaRepository<WorkFlowStep,Long> {
}
