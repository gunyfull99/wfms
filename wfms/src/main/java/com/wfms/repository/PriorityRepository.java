package com.wfms.repository;

import com.wfms.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PriorityRepository extends JpaRepository<Priority,Long> {
    @Query(value ="SELECT * FROM priority WHERE name = :priorityName LIMIT 1",nativeQuery = true)
    Priority findByPriorityId(@Param("priorityName") String priorityName);
}
