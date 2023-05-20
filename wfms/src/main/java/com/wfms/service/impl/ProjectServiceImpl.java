package com.wfms.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.*;
import com.wfms.config.Const;
import com.wfms.entity.*;
import com.wfms.repository.*;
import com.wfms.service.FireBaseService;
import com.wfms.service.ProjectService;
import com.wfms.service.UsersService;
import com.wfms.service.WorkFlowService;
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


import java.util.ArrayList;
 import java.time.LocalDateTime; 
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PriorityRepository priorityRepository;
//    @Autowired
//    private ProjectTypeRepository projectTypeRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Override
    public ObjectPaging findAllProject(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(),
                Sort.by("status").descending()
                        .and(Sort.by("priorityId").ascending())
                        .and(Sort.by("startDate").descending()));
        Page<Projects> projects =projectRepository.getProjectsByAdmin(objectPaging.getStatus(),objectPaging.getKeyword(),pageable);
        List<ProjectDTO> projectDTO=convert(projects.getContent());
        return ObjectPaging.builder().total((int) projects.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(projectDTO).build();
    }

    @Override
    public ObjectPaging findAllProjectByLead(String token, ObjectPaging objectPaging) {
       String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;

        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(),
                Sort.by("status").descending()
                        .and(Sort.by("priorityId").ascending())
                        .and(Sort.by("startDate").descending()));
        Page<Projects> projects =projectRepository.getProjectsByLead(users.getId(),objectPaging.getStatus(),objectPaging.getKeyword(),pageable);
        List<ProjectDTO> projectDTO=convert(projects.getContent());
        return ObjectPaging.builder().total((int) projects.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(projectDTO).build();
    }

    public  List<ProjectDTO> convert( List<Projects> list){
        List<ProjectDTO> projectDTO = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            List<UsersDto> userIds= new ArrayList<>();
            List<ProjectUsers> pu=projectUsersRepository.findAllByProjectId(list.get(i).getProjectId());
            for (int j = 0; j <pu.size() ; j++) {
                Users u =usersService.getById(pu.get(j).getUserId());
                UsersDto ud=new UsersDto();
                BeanUtils.copyProperties(u,ud);
                userIds.add(ud);
            }
            ProjectDTO projectDTO1 = new ProjectDTO();
            BeanUtils.copyProperties(list.get(i),projectDTO1);
            UsersDto usersDto = new UsersDto();
            BeanUtils.copyProperties(usersService.getById(list.get(i).getLead()),usersDto);
            Integer totalTask = taskRepository.getCountTaskByProject(projectDTO1.getProjectId());
            projectDTO1.setTotalTask(totalTask);
            projectDTO1.setLead(usersDto);
            projectDTO1.setUserId(userIds);
            projectDTO.add(projectDTO1);
        }
        return  projectDTO;
    }

    @Override
    public ProjectDTO getDetailProject(Long projectId) {
        Assert.notNull(projectId, Const.responseError.projectId_null);
        Projects projects= projectRepository.findById(projectId).get();
        Assert.notNull(projects,Const.responseError.project_notFound+projectId);
        List<UsersDto> userIds= new ArrayList<>();
        ProjectDTO projectDTO1 = new ProjectDTO();
        List<ProjectUsers> pu=projectUsersRepository.findAllByProjectId(projects.getProjectId());
            for (int j = 0; j <pu.size() ; j++) {
                Users u =usersService.getById(pu.get(j).getUserId());
                UsersDto ud=new UsersDto();
                BeanUtils.copyProperties(u,ud);
                userIds.add(ud);
        }
        BeanUtils.copyProperties(projects,projectDTO1);
        UsersDto usersDto = new UsersDto();
        BeanUtils.copyProperties(usersService.getById(projects.getLead()),usersDto);
        projectDTO1.setLead(usersDto);
        projectDTO1.setUserId(userIds);
        return projectDTO1;
    }

    @Override
    public Page<Projects> findProjectWithPageable(int total, int page) {
        Pageable pageable = PageRequest.of(page,total, Sort.Direction.DESC);
        return projectRepository.findAll(pageable);
    }

    @Override
    public Projects updateProject(Projects projectDTO) {
      //  Assert.isTrue(projectDTO.getProjectTypeId()!=null,"Loại dự án không được để trống");
        Assert.isTrue(projectDTO.getProjectId()!=null,Const.responseError.projectId_null);
        Assert.notNull(projectDTO.getLead(),Const.responseError.lead_null);
        Assert.notNull(projectDTO.getPriorityId(),Const.responseError.priorityId_null);
     //   Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
      //  ProjectType projectType = projectTypeRepository.findById(projectDTO.getProjectTypeId()).get();
      //  Assert.notNull(projectType,"Không tìm thấy projectType với id "+ projectDTO.getProjectTypeId());
        Priority priority=priorityRepository.findById(projectDTO.getPriorityId()).get();
        Projects projects = projectRepository.getById(projectDTO.getProjectId());
        Assert.notNull(priority,Const.responseError.priority_notFound+ projectDTO.getPriorityId());
        Assert.notNull(projects,Const.responseError.project_notFound+projectDTO.getProjectId());
        Users lead =usersService.findById(projectDTO.getLead());
        Assert.notNull(lead,Const.responseError.user_notFound+projectDTO.getLead());
        List<Sprint> sprintList= sprintRepository.findSprintByProjectIdAndNotClose(projectDTO.getProjectId());
        List<String>sprints=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(sprintList)){
            sprints=sprintList.stream().map(Sprint::getSprintName).collect(Collectors.toList());
        }
        Assert.isTrue(!(projectDTO.getStatus()==2 && DataUtils.listNotNullOrEmpty(sprintList)),"Have sprint not complete : "+ sprints);
        projectDTO.setStartDate(projects.getStartDate());
        if(projects.getStatus()==1 && projectDTO.getStatus()==3){
            projectDTO.setStartDate(LocalDateTime.now());
        }
        if(Objects.nonNull(projectDTO.getProjectName())&& !projectDTO.getProjectName().equals(projects.getProjectName())){
            Projects pn = projectRepository.getProjectByName(projectDTO.getProjectName().toLowerCase());
            Assert.isTrue(Objects.isNull(pn),"ProjecName is exsist");
        }
        if(Objects.nonNull(projectDTO.getShortName())&& !projectDTO.getShortName().equals(projects.getShortName())){
            Projects p1 = projectRepository.getProjectByShortName(projectDTO.getShortName().toLowerCase());
            Assert.isTrue(Objects.isNull(p1),"Project short name is exsist");
        }
        BeanUtils.copyProperties(projectDTO,projects);

        List<Task> taskList = taskRepository.getTaskByProjectIdAndStatus(projectDTO.getProjectId());
        List<String>tasks=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(taskList)){
            tasks=taskList.stream().map(Task::getCode).collect(Collectors.toList());
        }
        Assert.isTrue(!(projectDTO.getStatus()==2 && DataUtils.listNotNullOrEmpty(taskList)),"Have "+tasks.size()+" task not complete ");

        projects.setUpdateDate(LocalDateTime.now());

//        for (Long userId: projectDTO.getUserId()) {
//            ProjectUsers projectUsers = ProjectUsers.builder().projectId(projectId).userId(userId).build();
//            projectUsersRepository.save(projectUsers);
//        }
        return projectRepository.save(projects);
    }

    @Override
    public Projects startEndProject(Long projectId, Integer status) {
        Assert.notNull(projectId,Const.responseError.projectId_null);
        Projects projects = projectRepository.getById(projectId);
        Assert.notNull(projects,Const.responseError.project_notFound+projectId);
        List<Sprint> sprintList= sprintRepository.findSprintByProjectIdAndNotClose(projectId);
        List<String>sprints=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(sprintList)){
            sprints=sprintList.stream().map(Sprint::getSprintName).collect(Collectors.toList());
        }
        Assert.isTrue(!(status==2 && DataUtils.listNotNullOrEmpty(sprintList)),"Have sprint not complete : "+ sprints);
        projects.setStartDate(projects.getStartDate());
        if(projects.getStatus()==1 && status==3){
            projects.setStartDate(LocalDateTime.now());
        }
        List<Task> taskList = taskRepository.getTaskByProjectIdAndStatus(projectId);
        List<String>tasks=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(taskList)){
            tasks=taskList.stream().map(Task::getCode).collect(Collectors.toList());
        }
        Assert.isTrue(!(status==2 && DataUtils.listNotNullOrEmpty(taskList)),"Have "+tasks.size()+" task not complete ");
        projects.setUpdateDate(LocalDateTime.now());
        projects.setStatus(status);
        return projectRepository.save(projects);
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) throws FirebaseMessagingException {
     //   Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
        Assert.notNull(projectDTO.getLead(),Const.responseError.lead_null);
        Assert.notNull(projectDTO.getPriorityId(),Const.responseError.priorityId_null);
        Assert.notNull(projectDTO.getDeadLine(),Const.responseError.deadline_null);
        Assert.notNull(projectDTO.getProjectName(),"ProjectName must not be null");
        Projects pn = projectRepository.getProjectByName(projectDTO.getProjectName().toLowerCase());
        Assert.isTrue(Objects.isNull(pn),"ProjectName is exsist");
        if(projectDTO.getShortName()!=null){
            Projects p1 = projectRepository.getProjectByShortName(projectDTO.getShortName().toLowerCase());
            Assert.isTrue(Objects.isNull(p1),"Project short name is exsist");
        }
        // Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
     //   ProjectType projectType = projectTypeRepository.findById(projectDTO.getProjectTypeId()).get();
     //   Assert.notNull(projectType,"Không tìm thấy projectType với id "+ projectDTO.getProjectTypeId());
        Users lead =usersService.findById(projectDTO.getLead().getId());
        Assert.notNull(lead,Const.responseError.user_notFound+projectDTO.getLead().getId());
        List<String> role=lead.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        Assert.isTrue(role.contains("PM"),"User "+lead.getFullName()+ " don't have role PM");
        Projects projects = new Projects();
        BeanUtils.copyProperties(projectDTO,projects);
        projects.setShortName(projectDTO.getShortName()==null ? projectDTO.getProjectName() : projectDTO.getShortName());
        projects.setProjectId(null);
        projects.setStatus(1);
        projects.setCreateDate(LocalDateTime.now());
        projects.setLead(projectDTO.getLead().getId());
        Projects p = projectRepository.save(projects);
        List<Notification> notificationEntities =new ArrayList<>();
        if(Objects.nonNull(projectDTO.getUserId())){
            for (UsersDto userId: projectDTO.getUserId()) {
                Users u =usersService.findById(userId.getId());
                Assert.notNull(u,Const.responseError.user_notFound+userId.getId());
                ProjectUsers projectUsers = ProjectUsers.builder()
                        .projectId(p.getProjectId())
                        .userId(userId.getId())
                        .status(1)
                        .createDate(LocalDateTime.now())
                        .build();
                projectUsersRepository.save(projectUsers);
                notificationEntities.add(Notification.builder()
                        .projectId(p.getProjectId())
                        .userId(userId.getId())
                        .title("Add to project "+p.getProjectName())
                        .description("You have been added to the project "+p.getProjectName())
                        .status(1)
                        .timeRecive(LocalDateTime.now())
                        .createDate(LocalDateTime.now())
                        .build());
            }
            MessageDto messageDtoList =   MessageDto.builder().userId(projectDTO.getUserId().stream().map(UsersDto::getId).collect(Collectors.toList()))
                    .notification(NotificationDto.builder().title("Add to project "+p.getProjectName()).body("You have been added to the project "+p.getProjectName()).build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
            notificationRepository.saveAll(notificationEntities);
        }
        ProjectUsers projectUsers = ProjectUsers.builder()
                .projectId(p.getProjectId())
                .userId(lead.getId())
                .status(1)
                .createDate(LocalDateTime.now())
                .build();
        projectUsersRepository.save(projectUsers);

        workFlowService.createWorkFlow(WorkFlowDTO.builder().projectId(p.getProjectId()).build());
        BeanUtils.copyProperties(p,projectDTO);
        return projectDTO;
    }

    @Override
    public String removeUserFromProject(ProjectUserDTO projectUserDTO) throws FirebaseMessagingException {
        Assert.notNull(projectUserDTO.getProjectId(),Const.responseError.projectId_null);
        ProjectDTO p1 =getDetailProject(projectUserDTO.getProjectId());
        Assert.notNull(p1,Const.responseError.project_notFound+projectUserDTO.getProjectId());
        Assert.notNull(projectUserDTO.getUserId(),Const.responseError.userId_null);
        List<Notification> notificationEntities =new ArrayList<>();
        for (Long userId: projectUserDTO.getUserId()) {
            ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(userId,projectUserDTO.getProjectId());
            Assert.notNull(projectUsers,Const.responseError.user_notFound+userId+" in this project");
            projectUsers.setStatus(0);
                projectUsers.setUpdateDate(LocalDateTime.now());
                projectUsersRepository.save(projectUsers);
            notificationEntities.add(Notification.builder()
                    .projectId(projectUserDTO.getProjectId())
                    .userId(userId)
                    .title("Remove from project "+p1.getProjectName())
                    .description("You have been remove from the project "+p1.getProjectName())
                    .status(1)
                    .timeRecive(LocalDateTime.now())
                    .createDate(LocalDateTime.now())
                    .build());
        }
        MessageDto messageDtoList =   MessageDto.builder().userId(projectUserDTO.getUserId())
                .notification(NotificationDto.builder().title("Remove from project "+p1.getProjectName()).body("You have been remove from the project "+p1.getProjectName()).build()).build();
        fireBaseService.sendManyNotification(messageDtoList);
        notificationRepository.saveAll(notificationEntities);
        return "Delete successfull";

    }

    @Override
    public String addUserToProject(ProjectUserDTO projectUserDTO) throws FirebaseMessagingException {
        Assert.notNull(projectUserDTO.getProjectId(),Const.responseError.projectId_null);
        ProjectDTO p1 =getDetailProject(projectUserDTO.getProjectId());
        Assert.notNull(p1,Const.responseError.project_notFound+projectUserDTO.getProjectId());
        Assert.notNull(projectUserDTO.getUserId(),Const.responseError.userId_null);
        List<Notification> notificationEntities =new ArrayList<>();
        for (Long userId: projectUserDTO.getUserId()) {
            ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(userId,projectUserDTO.getProjectId());
            if(Objects.nonNull(projectUsers)){
                Assert.isTrue(projectUsers.getStatus()==0,"Member already in this project");
                projectUsers.setStatus(1);
                projectUsersRepository.save(projectUsers);
            }else{
                ProjectUsers p=  ProjectUsers.builder()
                        .status(1)
                        .createDate(LocalDateTime.now())
                        .projectId(projectUserDTO.getProjectId())
                        .userId(userId).build();
                projectUsersRepository.save(p);
            }
            notificationEntities.add(Notification.builder()
                    .projectId(projectUserDTO.getProjectId())
                    .userId(userId)
                    .title("Add to project "+p1.getProjectName())
                    .description("You have been added to the project "+p1.getProjectName())
                    .status(1)
                    .timeRecive(LocalDateTime.now())
                    .createDate(LocalDateTime.now())
                    .build());
        }
        MessageDto messageDtoList =   MessageDto.builder().userId(projectUserDTO.getUserId())
                .notification(NotificationDto.builder().title("Add to project "+p1.getProjectName()).body("You have been added to the project "+p1.getProjectName()).build()).build();
        fireBaseService.sendManyNotification(messageDtoList);
        notificationRepository.saveAll(notificationEntities);

        return "Add User successfull";
    }

    @Override
    public ObjectPaging getProjectByMember(String token, ObjectPaging objectPaging) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(),Sort.by("status").descending()
                .and(Sort.by("priorityId").ascending())
                .and(Sort.by("startDate").descending()));
       List<Long> listProjectId=projectUsersRepository.findAllByUserId(users.getId()).stream().map(ProjectUsers::getProjectId).collect(Collectors.toList());
        Page<Projects> projects =projectRepository.getProjectsByMember(listProjectId,objectPaging.getStatus(),objectPaging.getKeyword(),pageable);
        List<ProjectDTO> projectDTO=convert(projects.getContent());
        return ObjectPaging.builder().total((int) projects.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(projectDTO).build();
    }
}
