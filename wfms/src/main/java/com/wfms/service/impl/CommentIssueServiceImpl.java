package com.wfms.service.impl;

import com.wfms.entity.CommentIssue;
import com.wfms.repository.CommentIssueRepository;
import com.wfms.service.CommentIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentIssueServiceImpl implements CommentIssueService {
    @Autowired
    private CommentIssueRepository commentIssueRepository;
    @Override
    public List<CommentIssue> findAll() {
        return commentIssueRepository.findAll();
    }

    @Override
    public Page<CommentIssue> findWithPage(int page, int total) {
        Pageable pageable = PageRequest.of(page,total, Sort.Direction.DESC);
        return commentIssueRepository.findAll(pageable);
    }

    @Override
    public List<CommentIssue> findCommentIssueByIssueId(Long issueId) {
        return commentIssueRepository.findByIssueId(issueId);
    }
}
