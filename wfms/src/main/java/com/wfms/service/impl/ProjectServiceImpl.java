package com.wfms.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.*;
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
import java.util.Date;
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
    private NewsRepository newsRepository;
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
       Projects projects= projectRepository.findById(projectId).get();
        Assert.notNull(projects,"Không tìm thấy project id "+projectId);
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
        Assert.isTrue(projectDTO.getProjectId()!=null,"ID dự án không được để trống");
        Assert.notNull(projectDTO.getLead(),"Người quản lý dự án không được để trống");
        Assert.notNull(projectDTO.getPriorityId(),"Mức độ ưu tiên dự án không được để trống");
     //   Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
      //  ProjectType projectType = projectTypeRepository.findById(projectDTO.getProjectTypeId()).get();
      //  Assert.notNull(projectType,"Không tìm thấy projectType với id "+ projectDTO.getProjectTypeId());
        Priority priority=priorityRepository.findById(projectDTO.getPriorityId()).get();
        Projects projects = projectRepository.getById(projectDTO.getProjectId());
        Assert.notNull(priority,"Không tìm thấy priority với id "+ projectDTO.getPriorityId());
        Assert.notNull(projects,"Không tìm thấy project với ID "+projectDTO.getProjectId());
        Users lead =usersService.findById(projectDTO.getLead());
        Assert.notNull(lead,"Không tìm thấy lead với userId "+projectDTO.getLead());
        List<Sprint> sprintList= sprintRepository.findSprintByProjectIdAndNotClose(projectDTO.getProjectId());
        List<String>sprints=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(sprintList)){
            sprints=sprintList.stream().map(Sprint::getSprintName).collect(Collectors.toList());
        }
        Assert.isTrue(!(projectDTO.getStatus()==2 && DataUtils.listNotNullOrEmpty(sprintList)),"Have sprint not complete : "+ sprints);
        projectDTO.setStartDate(projects.getStartDate());
        if(projects.getStatus()==1 && projectDTO.getStatus()==3){
            projectDTO.setStartDate(new Date());
        }
        if(Objects.nonNull(projectDTO.getProjectName())&& !projectDTO.getProjectName().equals(projects.getProjectName())){
            Projects pn = projectRepository.getProjectByName(projectDTO.getProjectName().toLowerCase());
            Assert.isTrue(Objects.isNull(pn),"ProjecName đã tồn tại");
        }
        if(Objects.nonNull(projectDTO.getShortName())&& !projectDTO.getShortName().equals(projects.getShortName())){
            Projects p1 = projectRepository.getProjectByShortName(projectDTO.getShortName().toLowerCase());
            Assert.isTrue(Objects.isNull(p1),"Project short name đã tồn tại");
        }
        BeanUtils.copyProperties(projectDTO,projects);

        List<Task> taskList = taskRepository.getTaskByProjectIdAndStatus(projectDTO.getProjectId());
        List<String>tasks=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(taskList)){
            tasks=taskList.stream().map(Task::getCode).collect(Collectors.toList());
        }
        Assert.isTrue(!(projectDTO.getStatus()==2 && DataUtils.listNotNullOrEmpty(taskList)),"Have "+tasks.size()+" task not complete ");

        projects.setUpdateDate(new Date());

//        for (Long userId: projectDTO.getUserId()) {
//            ProjectUsers projectUsers = ProjectUsers.builder().projectId(projectId).userId(userId).build();
//            projectUsersRepository.save(projectUsers);
//        }
        return projectRepository.save(projects);
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) throws FirebaseMessagingException {
     //   Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
        Assert.notNull(projectDTO.getLead(),"Người quản lý dự án không được để trống");
        Assert.notNull(projectDTO.getPriorityId(),"Mức độ ưu tiên dự án không được để trống");
        Assert.notNull(projectDTO.getDeadLine(),"Deadline không được để trống");
        Assert.notNull(projectDTO.getProjectName(),"ProjectName không được để trống");
        Projects pn = projectRepository.getProjectByName(projectDTO.getProjectName().toLowerCase());
        Assert.isTrue(Objects.isNull(pn),"ProjectName đã tồn tại");
        if(projectDTO.getShortName()!=null){
            Projects p1 = projectRepository.getProjectByShortName(projectDTO.getShortName().toLowerCase());
            Assert.isTrue(Objects.isNull(p1),"Project short name đã tồn tại");
        }
        // Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
     //   ProjectType projectType = projectTypeRepository.findById(projectDTO.getProjectTypeId()).get();
     //   Assert.notNull(projectType,"Không tìm thấy projectType với id "+ projectDTO.getProjectTypeId());
        Assert.notNull(projectDTO.getPriorityId(),"Mức độ ưu tiên dự án không được để trống");
        Priority priority=priorityRepository.findById(projectDTO.getPriorityId()).get();
        Users lead =usersService.findById(projectDTO.getLead().getId());
        Assert.notNull(lead,"Không tìm thấy lead với userId "+projectDTO.getLead().getId());
        List<String> role=lead.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        Assert.isTrue(role.contains("PM"),"User "+lead.getFullName()+ " không có quyền PM");
        Projects projects = new Projects();
        BeanUtils.copyProperties(projectDTO,projects);
        projects.setShortName(projectDTO.getShortName()==null ? projectDTO.getProjectName() : projectDTO.getShortName());
        projects.setProjectId(null);
        projects.setStatus(1);
        projects.setCreateDate(new Date());
        projects.setLead(projectDTO.getLead().getId());
        Projects p = projectRepository.save(projects);
        List<News> newsEntitys=new ArrayList<>();
        if(Objects.nonNull(projectDTO.getUserId())){
            for (UsersDto userId: projectDTO.getUserId()) {
                Users u =usersService.findById(userId.getId());
                Assert.notNull(u,"Không tìm thấy member với userId "+userId.getId());
                ProjectUsers projectUsers = ProjectUsers.builder()
                        .projectId(p.getProjectId())
                        .userId(userId.getId())
                        .status(1)
                        .createDate(new Date())
                        .build();
                projectUsersRepository.save(projectUsers);
                newsEntitys.add(News.builder()
                        .projectId(p.getProjectId())
                        .userId(userId.getId())
                        .title("Add to project "+p.getProjectName())
                        .description("You have been added to the project "+p.getProjectName())
                        .status(1)
                        .timeRecive(new Date())
                        .createDate(new Date())
                        .build());
            }
            MessageDto messageDtoList =   MessageDto.builder().userId(projectDTO.getUserId().stream().map(UsersDto::getId).collect(Collectors.toList()))
                    .notification(NotificationDto.builder().title("Add to project "+p.getProjectName()).body("You have been added to the project "+p.getProjectName()).build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
            newsRepository.saveAll(newsEntitys);
        }
        ProjectUsers projectUsers = ProjectUsers.builder()
                .projectId(p.getProjectId())
                .userId(lead.getId())
                .status(1)
                .createDate(new Date())
                .build();
        projectUsersRepository.save(projectUsers);

        workFlowService.createWorkFlow(WorkFlowDTO.builder().projectId(p.getProjectId()).build());
        BeanUtils.copyProperties(p,projectDTO);
        return projectDTO;
    }

    @Override
    public String removeUserFromProject(ProjectUserDTO projectUserDTO) throws FirebaseMessagingException {
        Assert.notNull(projectUserDTO.getProjectId(),"Mã dự án không được để trống ");
        ProjectDTO p1 =getDetailProject(projectUserDTO.getProjectId());
        Assert.notNull(p1,"Không tìm thấy dự án với id "+projectUserDTO.getProjectId());
        Assert.notNull(projectUserDTO.getUserId(),"Mã nhân viên không được để trống ");
        List<News> newsEntitys=new ArrayList<>();
        for (Long userId: projectUserDTO.getUserId()) {
            ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(userId,projectUserDTO.getProjectId());
            Assert.notNull(projectUsers,"Không tìm thấy mã nhân viên "+userId+" trong project này");
            projectUsers.setStatus(0);
                projectUsers.setUpdateDate(new Date());
                projectUsersRepository.save(projectUsers);
            newsEntitys.add(News.builder()
                    .projectId(projectUserDTO.getProjectId())
                    .userId(userId)
                    .title("Remove from project "+p1.getProjectName())
                    .description("You have been remove from the project "+p1.getProjectName())
                    .status(1)
                    .timeRecive(new Date())
                    .createDate(new Date())
                    .build());
        }
        MessageDto messageDtoList =   MessageDto.builder().userId(projectUserDTO.getUserId())
                .notification(NotificationDto.builder().title("Remove from project "+p1.getProjectName()).body("You have been remove from the project "+p1.getProjectName()).build()).build();
        fireBaseService.sendManyNotification(messageDtoList);
        newsRepository.saveAll(newsEntitys);
        return "Xóa thành công";

    }

    @Override
    public String addUserToProject(ProjectUserDTO projectUserDTO) throws FirebaseMessagingException {
        Assert.notNull(projectUserDTO.getProjectId(),"Mã dự án không được để trống ");
        ProjectDTO p1 =getDetailProject(projectUserDTO.getProjectId());
        Assert.notNull(p1,"Không tìm thấy dự án với id "+projectUserDTO.getProjectId());
        Assert.notNull(projectUserDTO.getUserId(),"Mã nhân viên không được để trống ");
        List<News> newsEntitys=new ArrayList<>();
        for (Long userId: projectUserDTO.getUserId()) {
            ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(userId,projectUserDTO.getProjectId());
            if(Objects.nonNull(projectUsers)){
                Assert.isTrue(projectUsers.getStatus()==0,"Nhân viên đã trong dự án này");
                projectUsers.setStatus(1);
                projectUsersRepository.save(projectUsers);
            }else{
                ProjectUsers p=  ProjectUsers.builder()
                        .status(1)
                        .createDate(new Date())
                        .projectId(projectUserDTO.getProjectId())
                        .userId(userId).build();
                projectUsersRepository.save(p);
            }
            newsEntitys.add(News.builder()
                    .projectId(projectUserDTO.getProjectId())
                    .userId(userId)
                    .title("Add to project "+p1.getProjectName())
                    .description("You have been added to the project "+p1.getProjectName())
                    .status(1)
                    .timeRecive(new Date())
                    .createDate(new Date())
                    .build());
        }
        MessageDto messageDtoList =   MessageDto.builder().userId(projectUserDTO.getUserId())
                .notification(NotificationDto.builder().title("Add to project "+p1.getProjectName()).body("You have been added to the project "+p1.getProjectName()).build()).build();
        fireBaseService.sendManyNotification(messageDtoList);
        newsRepository.saveAll(newsEntitys);

        return "Add User thành công";
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
