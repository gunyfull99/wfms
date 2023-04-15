package com.wfms.service.impl;

import com.wfms.entity.IssueUsers;
import com.wfms.repository.IssueUsersRepository;
import com.wfms.service.IssueUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;

@Service
public class IssueUsersServiceImpl implements IssueUsersService {
    @Autowired
    private IssueUsersRepository issueUsersRepository;
    @Override
    public IssueUsers createIssueUser(IssueUsers issueUsers) {
        Assert.notNull(issueUsers.getIssueId()," IssueId không được để trống");
        Assert.notNull(issueUsers.getUserId()," UserId không được để trống");
        Assert.notNull(issueUsers.getIsResponsible()," IsResponsible không được để trống");
        issueUsers.setIssueUserId(null);
        issueUsers.setCreateDate(new Date());
        return issueUsersRepository.save(issueUsers);
    }
}
