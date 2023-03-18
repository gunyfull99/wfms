package com.wfms.service;

import com.wfms.Dto.CommentIssueDTO;
import com.wfms.entity.CommentIssue;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommentIssueService {
    List<CommentIssue> findAll();
    Page<CommentIssue> findWithPage(int page,int total);
    List<CommentIssue> findCommentIssueByIssueId(Long issueId);
    CommentIssueDTO createComment(String commentIssueDTO, List<MultipartFile> image);
    String getUrlFile(String name);
    byte[] getFile(String name);
    List<CommentIssueDTO> getCommentByIssue(Long issueId);
}
