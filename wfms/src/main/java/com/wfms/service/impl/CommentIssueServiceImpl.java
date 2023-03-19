package com.wfms.service.impl;

import com.wfms.Dto.CommentIssueDTO;
import com.wfms.entity.CommentIssue;
import com.wfms.entity.Issue;
import com.wfms.entity.Users;
import com.wfms.repository.CommentIssueRepository;
import com.wfms.repository.IssueRepository;
import com.wfms.service.CommentIssueService;
import com.wfms.service.UsersService;
import com.wfms.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CommentIssueServiceImpl implements CommentIssueService {
    @Autowired
    private CommentIssueRepository commentIssueRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UsersService usersService;
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

    @Override
    public CommentIssueDTO createComment(CommentIssueDTO commentIssueDTO) {
        Assert.notNull(commentIssueDTO.getContent(),"Content must not be null");
        Assert.notNull(commentIssueDTO.getIssueId(),"IssueId must not be null");
        Assert.notNull(commentIssueDTO.getUserId(),"UserId() must not be null");
        Issue issueData = issueRepository.getById(commentIssueDTO.getIssueId());
        Assert.notNull(issueData,"Not found IssueId "+commentIssueDTO.getIssueId());
        Users users =usersService.findById(commentIssueDTO.getUserId().getId());
        Assert.notNull(users,"Not found userId "+commentIssueDTO.getUserId().getId());
        String code = commentIssueDTO.getUserId().getId() + commentIssueDTO.getIssueId()+ DataUtils.generateTempPwd(9);

        //TODO
        CommentIssue commentIssue= new CommentIssue();
        BeanUtils.copyProperties(commentIssueDTO,commentIssue);
        commentIssue.setCommentIssueId(null);
        commentIssue.setCode(code);
        commentIssue.setCreateDate(new Date());
        commentIssue.setStatus(1);
        commentIssue.setUserId(commentIssueDTO.getUserId().getId());
        commentIssue=commentIssueRepository.save(commentIssue);
        BeanUtils.copyProperties(commentIssue,commentIssueDTO);

        return commentIssueDTO;
    }
}
