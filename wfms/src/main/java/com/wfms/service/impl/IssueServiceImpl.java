package com.wfms.service.impl;

import com.wfms.Dto.IssueDTO;
import com.wfms.entity.*;
import com.wfms.repository.IssueRepository;
import com.wfms.repository.IssueUsersRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.service.IssueService;
import com.wfms.service.IssueUsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class IssueServiceImpl implements IssueService {
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    @Autowired
    private IssueUsersRepository issueUsersRepository;
    @Autowired
    private IssueUsersService issueUsersService;
    @Override
    public List<Issue> getIssueByUserId(Long userId) {
        return issueRepository.getIssueByUserId(userId);
    }

    @Override
    public List<Issue> getIssueByUserIdAndProjectId(Long userId, Long projectId) {
        Assert.notNull(userId,"Mã nhân viên không được để trống");
        Assert.notNull(projectId,"Mã dự án không được để trống");
        return issueRepository.getIssueByUserIdAndProjectId(userId,projectId);
    }

    @Override
    public Issue createIssue(IssueDTO issue) {
        Assert.notNull(issue.getProjectId(),"Mã dự án không được để trống");
        Assert.notNull(issue.getPriorityId(),"Mức độ yêu cầu không được để trống");
        Assert.notNull(issue.getIssueTypeId(),"Loại task không được để trống");
        Assert.notNull(issue.getWorkFlowStepId(),"WorkFlowStep không được để trống");
        Assert.notNull(issue.getWorkFlowId(),"WorkFlowId không được để trống");
        Issue i = new Issue();
        BeanUtils.copyProperties(issue,i);
        i.setStatus(1);
        i.setCreatedDate(new Date());
        i.setIssueId(null);
        i.setArchivedBy(null);
        i.setArchivedDate(null);
        i.setIsArchived(false);
        if(Objects.nonNull(issue.getSprintId())){
            i.setSprint(new Sprint().builder().sprintId(issue.getSprintId()).build());
        }
        i.setPriority(new Priority().builder().priorityId(issue.getPriorityId()).build());
        i = issueRepository.save(i);
        if(Objects.nonNull(issue.getAssigness())){
            issueUsersService.createIssueUser(new IssueUsers().builder()
                    .issueId(i.getIssueId())
                    .userId(issue.getAssigness())
                    .isResponsible(true).build());
        }
        return i;
    }
    @Override
    public List<Issue> getIssueByProjectId(Long projectId) {
        Assert.notNull(projectId,"Mã dự án không được để trống");
        return issueRepository.getIssueByProjectId(projectId);
    }

    @Override
    public Issue getDetailIssueById(Long issueId) {
        Assert.notNull(issueId,"IssueID không được để trống");
        Issue issue = issueRepository.getById(issueId);
        Assert.notNull(issue,"Không tìm thấy task");
        return issue;
    }

    @Override
    public Issue updateTaskDoneOrNotDone(IssueDTO issue) {
        Assert.notNull(issue.getIssueId(),"Mã công việc không được để trống");
        Issue issueData = issueRepository.getById(issue.getIssueId());
        Assert.notNull(issueData,"Không tìm thấy công việc");
        ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(issueData.getAssigness(),issueData.getProjectId());
        Assert.notNull(projectUsers,"Không tìm thấy người làm công việc nên không thể chuyển công việc");
        issueData.setIsArchived(issue.getIsArchived());
        issueData.setUpdateDate(new Date());
        Issue issueUpdate = issueRepository.save(issueData);
        return issueUpdate;
    }

    @Override
    public IssueUsers updateAssignessTask(IssueUsers issueUsers) {
        Assert.notNull(issueUsers.getIssueId(),"Mã công việc không được để trống");
        Assert.notNull(issueUsers.getUserId(),"UserId không được để trống");
        Assert.notNull(issueUsers.getIsResponsible(),"IsResponsible không được để trống");
        Issue issueData = issueRepository.getById(issueUsers.getIssueId());
        Assert.notNull(issueData,"Không tìm thấy công việc");
        ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(issueData.getAssigness(),issueData.getProjectId());
        Assert.notNull(projectUsers,"Không tìm thấy member trong dự án");
        IssueUsers issueUsers1 = issueUsersRepository.findIssueUsersByUserIdAndIssueId(issueUsers.getUserId(),issueUsers.getIssueId());
        if(Objects.nonNull(issueUsers1)){
            Assert.notNull(issueUsers.getStatus(),"Status không được để trống");
            issueUsers.setUpdateDate(new Date());
            issueUsersRepository.save(issueUsers);
        }else{
            issueUsers.setStatus(1);
            issueUsers.setIsResponsible(true);
            issueUsers.setIssueUserId(null);
            issueUsers.setCreateDate(new Date());
        }
        BeanUtils.copyProperties(issueUsersRepository.save(issueUsers),issueUsers);
        return issueUsers;
    }

    @Override
    public List<Issue> getListTask(Long springId) {
        if(Objects.nonNull(springId)){
            return issueRepository.getListTaskInSprint(springId);
        }else{
            return issueRepository.getListTaskInBackLog();
        }
    }

}
