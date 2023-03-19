package com.wfms.service;

import com.wfms.Dto.CommentIssueDTO;
import com.wfms.entity.CommentIssue;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentIssueService {
    List<CommentIssue> findAll();
    Page<CommentIssue> findWithPage(int page,int total);
    List<CommentIssue> findCommentIssueByIssueId(Long issueId);
    CommentIssueDTO createComment(CommentIssueDTO commentIssueDTO);
}
