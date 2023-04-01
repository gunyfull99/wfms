package com.wfms.service.impl;

import com.wfms.Dto.IssueDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.SprintDTO;
import com.wfms.entity.*;
import com.wfms.repository.*;
import com.wfms.service.IssueService;
import com.wfms.service.IssueUsersService;
import com.wfms.service.SprintService;
import com.wfms.service.WorkFlowStepService;
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
import java.util.stream.Collectors;

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
    @Autowired
    private WorkFlowRepository workFlowRepository;
    @Autowired
    private WorkFlowStepRepository workFlowStepRepository;
    @Autowired
    private PriorityRepository priorityRepository;
    @Autowired
    private IssueTypeRepository issueTypeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WorkFlowStepService workFlowStepService;
    @Autowired
    private SprintService sprintService;


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
        Projects p = projectRepository.findById(issue.getProjectId()).get();
        Assert.notNull(p,"Không tìm thấy dự án với id "+ issue.getProjectId());
        Priority priority=priorityRepository.findById(issue.getPriorityId()).get();
        Assert.notNull(priority,"Không tìm thấy priority với id "+ issue.getPriorityId());
        IssueTypes issueTypes= issueTypeRepository.findById(issue.getIssueTypeId()).get();
        Assert.notNull(issueTypes,"Không tìm thấy issueType với id "+ issue.getIssueTypeId());
        if(issue.getWorkFlowId()!=null){
            WorkFlow workFlow=workFlowRepository.findById(issue.getWorkFlowId()).get();
            Assert.notNull(workFlow,"Không tìm thấy WorkFlow với id "+ issue.getWorkFlowId());
        }
        if(issue.getWorkFlowStepId()!=null){
            WorkFlowStep workFlowStep = workFlowStepRepository.findById(issue.getWorkFlowStepId()).get();
            Assert.notNull(workFlowStep,"Không tìm thấy WorkFlowStep với id "+ issue.getWorkFlowStepId());
        }
        Integer count = issueRepository.getCountIssueByProject(p.getProjectId());
        Issue i = new Issue();
        BeanUtils.copyProperties(issue,i);
        i.setStatus(1);
        i.setCreatedDate(new Date());
        i.setIssueId(null);
        i.setCode(p.getShortName()+"-"+ (count==null ? 1:count+1));
        i.setArchivedBy(null);
        i.setArchivedDate(null);
        i.setIsArchived(false);
        if(Objects.nonNull(issue.getSprintId())){
            i.setSprint(Sprint.builder().sprintId(issue.getSprintId()).build());
        }
        i.setPriority(Priority.builder().priorityId(issue.getPriorityId()).build());
        i = issueRepository.save(i);
        if(Objects.nonNull(issue.getAssigness())){
            issueUsersService.createIssueUser(IssueUsers.builder()
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
    public IssueDTO getDetailIssueById(Long issueId) {
        Assert.notNull(issueId,"IssueID không được để trống");
        Issue issue = issueRepository.findById(issueId).get();
        Assert.notNull(issue,"Không tìm thấy task");
        IssueDTO issueDTO = new IssueDTO();
        BeanUtils.copyProperties(issue,issueDTO);
        issueDTO.setPriorityId(issue.getPriority().getPriorityId());
        return issueDTO;
    }

    @Override
    public Issue updateTask(IssueDTO issue) {
        Assert.notNull(issue.getIssueId(),"Mã công việc không được để trống");
        Issue issueData = issueRepository.getById(issue.getIssueId());
        Assert.notNull(issueData,"Không tìm thấy công việc");
        List<Long> workFlowStep=workFlowStepService.listWorkFlowStep(issueData.getWorkFlowId())
                        .stream().map(WorkFlowStep :: getWorkFlowStepId).collect(Collectors.toList());
        Assert.isTrue(workFlowStep.contains(issue.getWorkFlowStepId()),"WorkFlowStep không trong WorkFlow hiện tại");
     //   ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(issueData.getAssigness(),issueData.getProjectId());
      //  Assert.notNull(projectUsers,"Không tìm thấy người làm công việc nên không thể chuyển công việc");
        if(issue.getSprintId()!=null){
          List<Long>sprintId= sprintService.findSprintByProjectId(issueData.getProjectId())
                  .stream().map(SprintDTO::getSprintId).collect(Collectors.toList());
          Assert.isTrue(sprintId.contains(issue.getSprintId()),"Sprint không trong project hiện tại");
            issueData.setSprint(Sprint.builder().sprintId(issue.getSprintId()).build());
        }
        issueData.setUpdateDate(new Date());
        issueData.setDescription(issue.getDescription());
        issueData.setSummary(issue.getSummary());
        issueData.setWorkFlowStepId(issue.getWorkFlowStepId());
        return issueRepository.save(issueData);
    }

    @Override
    public List<IssueUsers> updateAssignessTask(List<IssueUsers> issueUser) {
        if(DataUtils.listNotNullOrEmpty(issueUser)){
            List<IssueUsers>count=issueUser.stream().filter(IssueUsers::getIsResponsible).collect(Collectors.toList());
            Assert.isTrue(count.size()==1,"Số người làm chính phải là 1");
            issueUser.forEach(issueUsers -> {
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
            });
        }
        return issueUser;
    }

    @Override
    public List<Issue> getListTask(Long projectId,Long springId) {
        if(Objects.nonNull(springId)){
            return issueRepository.getListTaskInSprint(springId);
        }else{
            return issueRepository.getListTaskInBackLog(projectId);
        }
    }

    @Override
    public ObjectPaging searchIssue(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("issueId").descending());
        Page<Issue> list =issueRepository.searchIssuePaging(objectPaging.getProjectId(), objectPaging.getStatus(), objectPaging.getKeyword(),pageable);
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(list.getContent()).build();
    }

}
