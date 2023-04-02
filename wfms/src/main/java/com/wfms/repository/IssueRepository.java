package com.wfms.repository;

import com.wfms.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query(value = "SELECT issue.* FROM issue, issue_users, users where users.id = " +
            "issue_users.user_id and issue.issue_id =issue_users.issue_id and users.id = :userId",nativeQuery = true)
    List<Issue> getIssueByUserId(@Param("userId") Long userId);
    @Query(value = "SELECT issue.* FROM issue, issue_users, users where users.id = " +
            "issue_users.user_id and issue.issue_id =issue_users.issue_id and users.id = :userId and issue.project_id = :projectId",nativeQuery = true)
    List<Issue> getIssueByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    @Query(value = "Select * from issue where project_id = :projectId",nativeQuery = true)
    List<Issue> getIssueByProjectId(@Param("projectId") Long projectId);

    @Query(value = "Select i from Issue i where  " +
            " (:projectId is null OR (i.projectId)= :projectId)" +
            "and (:status is null OR (i.status) = :status) " +
            "and (:keyword is null OR LOWER(i.description) LIKE %:keyword% " +
            "or LOWER(i.summary) LIKE %:keyword% " +
            "or  LOWER(i.code) LIKE %:keyword% ) ")
    Page<Issue> searchIssuePaging(@Param("projectId") Long projectId,@Param("status") Integer status,@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "Select i from Issue i where  (:sprintId is null OR i.sprint.sprintId = :sprintId) ")
    List<Issue> getListTaskInSprint(@Param("sprintId") Long sprintId);

    @Query(value = "Select * from issue where  sprint_id is null and project_id = :projectId",nativeQuery = true)
    List<Issue> getListTaskInBackLog( @Param("projectId") Long projectId);

    @Query(value = "Select count(*) from issue where project_id = :projectId",nativeQuery = true)
    Integer getCountIssueByProject( @Param("projectId") Long projectId);

    @Query(value = "Select * from issue where dead_line = :deadLine",nativeQuery = true)
    List<Issue> getIssueByDeadline(@Param("deadLine") Date deadLine);
}

