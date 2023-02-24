package com.wfms.repository;

import com.wfms.entity.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowRepository extends JpaRepository<WorkFlow,Long> {
}
