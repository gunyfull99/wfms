package com.wfms.repository;

import com.wfms.entity.ProjectUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectUsersRepository extends JpaRepository<ProjectUsers,Long> {
    @Query(value = "SELECT * FROM project_users WHERE user_id = :userId and project_id= :projectId",nativeQuery = true)
    ProjectUsers getProjectUersByUserIdAndProjectId(@Param("userId") Long userId,@Param("projectId") Long projectId);
}
