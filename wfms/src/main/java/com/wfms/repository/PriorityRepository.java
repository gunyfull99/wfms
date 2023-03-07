package com.wfms.repository;

import com.wfms.entity.Priority;
import com.wfms.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PriorityRepository extends JpaRepository<Priority,Long> {
}
