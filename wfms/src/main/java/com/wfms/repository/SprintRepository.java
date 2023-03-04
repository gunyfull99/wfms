package com.wfms.repository;

import com.wfms.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint,Long> {
    @Query(value = "Select * from sprint where project_id= :projectId ",nativeQuery = true)
    List<Sprint> findSprintByProjectId(@Param("projectId") Long projectId);
}
