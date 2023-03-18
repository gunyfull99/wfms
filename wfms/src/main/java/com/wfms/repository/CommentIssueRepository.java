package com.wfms.repository;

import com.wfms.entity.CommentIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentIssueRepository extends JpaRepository<CommentIssue,Long> {
    @Query(value = "SELECT * FROM comment_issue where issue_id = :issueId",nativeQuery = true)
    List<CommentIssue> findByIssueId(@Param("issueId") Long issueId);


}
