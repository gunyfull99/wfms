package com.wfms.repository;

import com.wfms.entity.IssueUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueUsersRepository extends JpaRepository<IssueUsers, Long> {
    @Query(value = "select * from issue_users where user_id = :userId and issue_id = :issueId",nativeQuery = true)
    IssueUsers findIssueUsersByUserIdAndIssueId(@Param("userId")Long userId, @Param("issueId")Long issueId);
}
