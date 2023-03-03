package com.wfms.repository;

import com.wfms.entity.CommentIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentIssueRepository extends JpaRepository<CommentIssue,Long> {
    List<CommentIssue> findByIssueId(Long issueId);
}
