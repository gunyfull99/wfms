package com.wfms.repository;

import com.wfms.entity.ProjectUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectUsersRepository extends JpaRepository<ProjectUsers,Long> {
    @Query(value = "SELECT * FROM project_users WHERE user_id = :userId and project_id= :projectId and status = 1",nativeQuery = true)
    ProjectUsers getProjectUersByUserIdAndProjectId(@Param("userId") Long userId,@Param("projectId") Long projectId);

    List<ProjectUsers> findAllByProjectIdAndStatus(Long projectId, Integer status);
}
