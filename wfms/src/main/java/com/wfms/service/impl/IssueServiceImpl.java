package com.wfms.service.impl;

import com.wfms.Dto.*;
import com.wfms.Dto.ChartResponseDto;
import com.wfms.Dto.IssueDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.SprintDTO;
import com.wfms.entity.*;
import com.wfms.repository.*;
import com.wfms.service.*;
import com.wfms.utils.DataUtils;
import com.wfms.utils.JwtUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
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
    private LevelRepository levelRepository;
    @Autowired
    private IssueTypeRepository issueTypeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WorkFlowStepService workFlowStepService;
    @Autowired
    private SprintService sprintService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtUtility jwtUtility;
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
    public Issue createIssue(String token, IssueDTO issue) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        List<String> role=users.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        Assert.notNull(issue.getProjectId(),"Mã dự án không được để trống");
        Assert.notNull(issue.getPriorityId(),"Mức độ yêu cầu không được để trống");
        Assert.notNull(issue.getLevelId(),"Độ khó không được để trống");
        Assert.notNull(issue.getIssueTypeId(),"Loại task không được để trống");
        Projects p = projectRepository.findById(issue.getProjectId()).get();
        Assert.notNull(p,"Không tìm thấy dự án với id "+ issue.getProjectId());
        Priority priority=priorityRepository.findById(issue.getPriorityId()).get();
        Assert.notNull(priority,"Không tìm thấy priority với id "+ issue.getPriorityId());
        Level level=levelRepository.findById(issue.getLevelId()).get();
        Assert.notNull(level,"Không tìm thấy level với id "+ issue.getLevelId());
        IssueTypes issueTypes= issueTypeRepository.findById(issue.getIssueTypeId()).get();
        Assert.notNull(issueTypes,"Không tìm thấy issueType với id "+ issue.getIssueTypeId());
        WorkFlow workFlow=workFlowRepository.getDetailWorkflow(issue.getProjectId());
        Assert.notNull(workFlow,"Không tìm thấy WorkFlow ");
        Integer count = issueRepository.getCountIssueByProject(p.getProjectId());
        Issue i = new Issue();
        BeanUtils.copyProperties(issue,i);
        i.setCreatedDate(new Date());
        i.setIssueId(null);
        i.setWorkFlowId(workFlow.getWorkFlowId());
        i.setCode(p.getShortName()+"-"+ (count==null ? 1:count+1));
        i.setArchivedBy(null);
        i.setArchivedDate(null);
        i.setIsArchived(false);
        WorkFlowStep workFlowStep = workFlowStepRepository.getWorkFLowStepStart(workFlow.getWorkFlowId());
        Assert.notNull(workFlowStep,"Không tìm thấy step start ");
        i.setWorkFlowStepId(workFlowStep.getWorkFlowStepId());
        if(Objects.nonNull(issue.getSprintId())){
            SprintDTO sprintDTO=sprintService.getDetailSprint(issue.getSprintId());
            i.setSprint(Sprint.builder().sprintId(sprintDTO.getSprintId()).build());
        }
        i.setPriority(Priority.builder().priorityId(issue.getPriorityId()).build());
        i.setLevelId(issue.getLevelId());

        if(role.contains("PM")){
            i.setCreateByPm(true);
            if(Objects.nonNull(issue.getAssigness())) {
                i.setStatus(3);
            }else{
                i.setStatus(1);
            }
        }else{
            i.setAssigness(users.getId());
            i.setCreateByPm(false);
            i.setStatus(1);

        }
        i.setReporter(users.getId());
        i = issueRepository.save(i);
        if(Objects.nonNull(issue.getAssigness())){
            if(role.contains("PM")){
                issueUsersService.createIssueUser(IssueUsers.builder()
                        .issueId(i.getIssueId())
                        .userId(issue.getAssigness())
                        .status(2)
                        .isResponsible(true).build());
            }else{
                issueUsersService.createIssueUser(IssueUsers.builder()
                        .issueId(i.getIssueId())
                        .userId(users.getId())
                        .status(1)
                        .isResponsible(true).build());
            }
        }
        return i;
    }
    @Override
    public List<IssueDTO> getIssueByProjectId(Long projectId) {
        Assert.notNull(projectId,"Mã dự án không được để trống");
        List<Issue> issue=issueRepository.getIssueByProjectId(projectId);
        return convert(issue);
    }

    @Override
    public IssueDTO getDetailIssueById(Long issueId) {
        Assert.notNull(issueId,"IssueID không được để trống");
        Issue issue = issueRepository.findById(issueId).get();
        Assert.notNull(issue,"Không tìm thấy task");
        return convert(List.of(issue)).get(0);
    }

    @Override
    public Issue updateTask(String token,IssueDTO issue) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        List<String> role=users.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
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
        if(!Objects.equals(issueData.getWorkFlowStepId(), issue.getWorkFlowStepId())){
            if(issueData.getStatus()!=3){
                Assert.isTrue(role.contains("PM"),"Bạn không có quyền thực hiện công việc update step");
            }
        }
        if(Objects.nonNull(issue.getStatus()) && role.contains("PM")){
            issueData.setStatus(issue.getStatus());
        }
        if(Objects.nonNull(issue.getIssueTypeId()) && role.contains("PM")){
            issueData.setIssueTypeId(issue.getIssueTypeId());
        }
        if(Objects.nonNull(issue.getLevelId()) && role.contains("PM")){
            issueData.setLevelId(issue.getLevelId());
        }
        issueData.setUpdateDate(new Date());
        issueData.setDescription(issue.getDescription());
        issueData.setSummary(issue.getSummary());
        issueData.setWorkFlowStepId(issue.getWorkFlowStepId());
        return issueRepository.save(issueData);
    }

    @Override
    public List<IssueUsers> updateAssignessTask( List<IssueUsers> issueUser) {
        if(DataUtils.listNotNullOrEmpty(issueUser)){
            Issue issueData = issueRepository.getById(issueUser.get(0).getIssueId());
            Assert.notNull(issueData,"Không tìm thấy công việc");
            List<IssueUsers>count=issueUser.stream().filter(IssueUsers::getIsResponsible).collect(Collectors.toList());
            if(issueData.getStatus()==1){
                Assert.isTrue(count.size()==1 || count.size()==0,"Số người làm chính phải là 1");
            }else{
                Assert.isTrue(count.size()==1,"Số người làm chính phải là 1");
            }
            List<Long>checkIssueId=issueUser.stream().map(IssueUsers::getIssueId).distinct().collect(Collectors.toList());
            Assert.isTrue(checkIssueId.size()==1,"Danh sách member đang không cùng 1 issue");

            issueUser.forEach(issueUsers -> {
                Assert.notNull(issueUsers.getIssueId(),"Mã công việc không được để trống");
                Assert.notNull(issueUsers.getUserId(),"UserId không được để trống");
                Assert.notNull(issueUsers.getIsResponsible(),"IsResponsible không được để trống");
                Assert.notNull(issueUsers.getStatus(),"Status không được để trống");
                ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(issueUsers.getUserId(),issueData.getProjectId());
                Assert.notNull(projectUsers,"Không tìm thấy member trong dự án");
                IssueUsers issueUsers1 = issueUsersRepository.findIssueUsersByUserIdAndIssueId(issueUsers.getUserId(),issueUsers.getIssueId());
                if(Objects.nonNull(issueUsers1)){
                    issueUsers1.setStatus(issueUsers.getStatus());
                    issueUsers1.setIsResponsible(issueUsers.getIsResponsible());
                    issueUsers1.setUpdateDate(new Date());
                    issueUsersRepository.save(issueUsers1);
                }else{
                    issueUsersService.createIssueUser(IssueUsers.builder()
                            .issueId(issueUsers.getIssueId())
                            .userId(issueUsers.getUserId())
                            .status(issueUsers.getStatus())
                            .isResponsible(issueUsers.getIsResponsible()).build());
                }

            });
            if(issueData.getStatus()==1 && count.size()==1){
                issueData.setStatus(3);
                issueRepository.save(issueData);
            }
        }
        return issueUser;
    }

    @Override
    public List<IssueDTO> getListTask(Long projectId,Long springId) {
        if(Objects.nonNull(springId)){
            List<Issue> issue= issueRepository.getListTaskInSprint(springId);
            return convert(issue);
        }else{
            List<Issue> issue= issueRepository.getListTaskInBackLog(projectId);
            return convert(issue);
        }
    }
    @Override
    public ObjectPaging searchIssue(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("issueId").descending());
        Page<Issue> list;
        if(Objects.nonNull(objectPaging.getSprintId()) && objectPaging.getSprintId()== -1){
            list =issueRepository.searchIssuePagingBackLog(objectPaging.getProjectId(), objectPaging.getStatus(), objectPaging.getKeyword(),objectPaging.getSprintId(),objectPaging.getStepId(),objectPaging.getCreateByPm(),pageable);
        }else{
            list =issueRepository.searchIssuePaging(objectPaging.getProjectId(), objectPaging.getStatus(), objectPaging.getKeyword(),objectPaging.getSprintId(),objectPaging.getStepId(),objectPaging.getCreateByPm(),pageable);
        }
        List<IssueDTO> issueDTOList=convert(list.getContent());
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(issueDTOList).build();
    }

    @Override
    public List<List<ChartIssue>> chartIssue(Long projectId, Boolean inBackLog, Integer status) {
        Assert.notNull(projectId,"ProjectId không được để trống");
        List<SprintDTO> sprintDTOList = sprintService.findSprintByProjectId(projectId).stream().filter(sprintDTO -> sprintDTO.getStatus()==status).collect(Collectors.toList());
        List<Issue> issue1;
        List<List<ChartIssue>> list=new ArrayList<>();
        if(inBackLog){
            List<ChartIssue> chartIssues=new ArrayList<>();
            issue1= issueRepository.getListTaskInBackLog(projectId);
            if(DataUtils.listNotNullOrEmpty(issue1)){
                List<WorkFlowStep> step=workFlowStepService.listWorkFlowStep(issue1.get(0).getWorkFlowId());
                if(DataUtils.listNotNullOrEmpty(step)){
                    step.forEach(workFlowStep -> {
                        List<Issue> issue= issueRepository.getListTaskInBackLogAndStep(workFlowStep.getWorkFlowStepId());
                        ChartIssue chartIssue =  ChartIssue.builder()
                                .nameSprint("BackLog")
                                .name(workFlowStep.getWorkFLowStepName())
                                .color(workFlowStep.getColor())
                                .numberTask(issue.size()).build();
                        chartIssues.add(chartIssue);
                    });
                }
            }
            list.add(chartIssues);
        }else{
            if(DataUtils.listNotNullOrEmpty(sprintDTOList)){
                for (int i = 0; i < sprintDTOList.size(); i++) {
                    issue1= issueRepository.getListTaskInSprint(sprintDTOList.get(i).getSprintId());
                    List<ChartIssue> chartIssues=new ArrayList<>();
                    if(DataUtils.listNotNullOrEmpty(issue1)){
                        List<WorkFlowStep> step=workFlowStepService.listWorkFlowStep(issue1.get(0).getWorkFlowId());
                        if(DataUtils.listNotNullOrEmpty(step)){
                            int finalI = i;
                            step.forEach(workFlowStep -> {
                                List<Issue> issue= issueRepository.getListTaskInSprintAndStep(sprintDTOList.get(finalI).getSprintId(),workFlowStep.getWorkFlowStepId());
                                ChartIssue chartIssue =  ChartIssue.builder()
                                        .nameSprint(sprintDTOList.get(finalI).getSprintName())
                                        .name(workFlowStep.getWorkFLowStepName())
                                        .color(workFlowStep.getColor())
                                        .numberTask(issue.size()).build();
                                chartIssues.add(chartIssue);
                            });
                        }
                    }
                    list.add(chartIssues);
                }
            }
        }
        return list ;
    }

    @Override
    public String requestToIssue(String token, Long issueId) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Assert.notNull(issueId,"IssueId không được để trống");
        Issue issueData = issueRepository.findById(issueId).get();
        Assert.notNull(issueData,"Không tìm thấy công việc");
        ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(users.getId(),issueData.getProjectId());
        Assert.notNull(projectUsers,"Không tìm thấy member " +users.getFullName() +" trong dự án");
        issueUsersRepository.save(IssueUsers.builder()
                .status(1).
                userId(users.getId()).
                issueId(issueId).
                isResponsible(false).
                issueUserId(null).
                createDate(new Date()).build());
         return "Tạo yêu cầu vào task "+issueData.getCode() +" thành công";
    }

    public  List<IssueDTO> convert(List<Issue> list){
        List<IssueDTO> issueDTOList=new ArrayList<>();
        list.forEach(issue -> {
            IssueDTO issueDTO=new IssueDTO();
            BeanUtils.copyProperties(issue,issueDTO);
            Users reporter = usersService.getById(issue.getReporter());
            UsersDto reporter1 = new UsersDto();
            BeanUtils.copyProperties(reporter, reporter1);
            issueDTO.setReporter(reporter1);
            issueDTO.setPriorityId(issue.getPriority().getPriorityId());
            List<IssueUsers> issueUsers1 = issueUsersRepository.findIssueUsersByIssueId(issue.getIssueId());
            List<IssueUsersDTO> issueUsersDTOS = new ArrayList<>();
            List<UsersDto> userIds= new ArrayList<>();
            for (int j = 0; j <issueUsers1.size() ; j++) {
                IssueUsersDTO issueUsersDTO=new IssueUsersDTO();
                BeanUtils.copyProperties(issueUsers1.get(j), issueUsersDTO);
                Users u = usersService.getById(issueUsers1.get(j).getUserId());
                UsersDto ud = new UsersDto();
                BeanUtils.copyProperties(u, ud);
                userIds.add(ud);
                issueUsersDTO.setUserId(ud);
                issueUsersDTOS.add(issueUsersDTO);
            }
            if(Objects.nonNull(issue.getSprint())){
                issueDTO.setSprintId(issue.getSprint().getSprintId());
            }
            issueDTO.setUsersList(issueUsersDTOS);
            issueDTOList.add(issueDTO);
        });
        return issueDTOList;
    }
    @Override
    public List<ChartResponseDto> getstatisticTask(Long projectId) {
        Assert.notNull(projectId,"Mã dự án không được để trống");
        return issueRepository.getstatisticTask(projectId);
    }
}
