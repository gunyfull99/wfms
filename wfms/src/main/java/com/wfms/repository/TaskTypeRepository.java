package com.wfms.repository;

import com.wfms.entity.TaskTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskTypes,Long> {
}
