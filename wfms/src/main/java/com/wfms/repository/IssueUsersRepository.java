package com.wfms.repository;

import com.wfms.entity.IssueUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueUsersRepository extends JpaRepository<IssueUsers, Long> {
    @Query(value = "select * from issue_users where user_id = :userId and issue_id = :issueId",nativeQuery = true)
    IssueUsers findIssueUsersByUserIdAndIssueId(@Param("userId")Long userId, @Param("issueId")Long issueId);
    @Query(value = "select * from issue_users where issue_id = :issueId and is_responsible= :isResponsible and status = 2",nativeQuery = true)
    IssueUsers findIssueUsersByIssueIdAndIsResponsible(@Param("issueId")Long userId, @Param("isResponsible")Boolean isResponsible);
    @Query(value = "select * from issue_users where issue_id = :issueId and status = 2",nativeQuery = true)
    List<IssueUsers> findIssueUsersByIssueId(@Param("issueId")Long issueId);
}
