package com.wfms.repository;

import com.wfms.entity.Sprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint,Long> {
    @Query(value = "Select * from sprint where project_id= :projectId order by end_date desc,status desc ",nativeQuery = true)
    List<Sprint> findSprintByProjectId(@Param("projectId") Long projectId);

    @Query(value = "Select * from sprint where project_id= :projectId and status = 2",nativeQuery = true)
    List<Sprint> findSprintByProjectIdAndClose(@Param("projectId") Long projectId);
    @Query(value = "Select * from sprint where project_id= :projectId and status IN (1,3)",nativeQuery = true)
    List<Sprint> findSprintByProjectIdAndNotClose(@Param("projectId") Long projectId);


    @Query(value = "Select s from Sprint s where  " +
            " (:projectId is null OR (s.projects.projectId)= :projectId) and " +
            "(:status is null OR (s.status) = :status)  and " +
            "  (:keyword is null OR LOWER(s.sprintName) LIKE %:keyword%) ")
    Page<Sprint> findSprintByProjectId(@Param("projectId") Long projectId,@Param("status") Integer status,@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "Select * from sprint where sprint_id = :sprint_id  ",nativeQuery = true)
    Sprint getDetailSprintById(@Param("sprint_id") Long sprintId);
}
