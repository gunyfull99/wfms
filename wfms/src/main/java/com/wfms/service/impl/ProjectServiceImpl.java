package com.wfms.service.impl;

import com.wfms.Dto.*;
import com.wfms.entity.*;
import com.wfms.repository.*;
import com.wfms.service.ProjectService;
import com.wfms.service.UsersService;
import com.wfms.service.WorkFlowService;
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
    private IssueRepository issueRepository;
    @Autowired
    private PriorityRepository priorityRepository;
    @Autowired
    private ProjectTypeRepository projectTypeRepository;
    @Autowired
    private JwtUtility jwtUtility;

    @Override
    public ObjectPaging findAllProject(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(),
                Sort.by("priorityId").ascending()
                        .and(Sort.by("status").descending())
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
                Sort.by("priorityId").ascending()
                        .and(Sort.by("status").descending())
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
            Integer totalIssue = issueRepository.getCountIssueByProject(projectDTO1.getProjectId());
            projectDTO1.setTotalIssue(totalIssue);
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
        Assert.isTrue(projectDTO.getProjectTypeId()!=null,"Loại dự án không được để trống");
        Assert.isTrue(projectDTO.getProjectId()!=null,"ID dự án không được để trống");
        Assert.notNull(projectDTO.getLead(),"Người quản lý dự án không được để trống");
        Assert.notNull(projectDTO.getPriorityId(),"Mức độ ưu tiên dự án không được để trống");
        Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
        ProjectType projectType = projectTypeRepository.findById(projectDTO.getProjectTypeId()).get();
        Assert.notNull(projectType,"Không tìm thấy projectType với id "+ projectDTO.getProjectTypeId());
        Priority priority=priorityRepository.findById(projectDTO.getPriorityId()).get();
        Projects projects = projectRepository.getById(projectDTO.getProjectId());
        Assert.notNull(priority,"Không tìm thấy priority với id "+ projectDTO.getPriorityId());
        Assert.notNull(projects,"Không tìm thấy project với ID "+projectDTO.getProjectId());
        Users lead =usersService.findById(projectDTO.getLead());
        Assert.notNull(lead,"Không tìm thấy lead với userId "+projectDTO.getLead());
        BeanUtils.copyProperties(projectDTO,projects);

        projects.setUpdateDate(new Date());
//        for (Long userId: projectDTO.getUserId()) {
//            ProjectUsers projectUsers = ProjectUsers.builder().projectId(projectId).userId(userId).build();
//            projectUsersRepository.save(projectUsers);
//        }
        return projectRepository.save(projects);
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
        Assert.notNull(projectDTO.getLead(),"Người quản lý dự án không được để trống");
        Assert.notNull(projectDTO.getPriorityId(),"Mức độ ưu tiên dự án không được để trống");
        Assert.notNull(projectDTO.getProjectTypeId(),"Loại dự án không được để trống");
        ProjectType projectType = projectTypeRepository.findById(projectDTO.getProjectTypeId()).get();
        Assert.notNull(projectType,"Không tìm thấy projectType với id "+ projectDTO.getProjectTypeId());
        Priority priority=priorityRepository.findById(projectDTO.getPriorityId()).get();
        Assert.notNull(projectDTO.getPriorityId(),"Mức độ ưu tiên dự án không được để trống");
        Users lead =usersService.findById(projectDTO.getLead().getId());
        Assert.notNull(lead,"Không tìm thấy lead với userId "+projectDTO.getLead().getId());
        Projects projects = new Projects();
        BeanUtils.copyProperties(projectDTO,projects);
        projects.setShortName(projectDTO.getShortName()==null ? projectDTO.getProjectName() : projectDTO.getShortName());
        projects.setProjectId(null);
        projects.setStatus(1);
        projects.setCreateDate(new Date());
        projects.setLead(projectDTO.getLead().getId());
        Projects p = projectRepository.save(projects);
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
            }
        }

        workFlowService.createWorkFlow(WorkFlowDTO.builder().projectId(p.getProjectId()).build());
        BeanUtils.copyProperties(p,projectDTO);
        return projectDTO;
    }

    @Override
    public String removeUserFromProject(ProjectUserDTO projectUserDTO) {
        Assert.notNull(projectUserDTO.getProjectId(),"Mã dự án không được để trống ");
        Assert.notNull(projectUserDTO.getUserId(),"Mã nhân viên không được để trống ");
        for (Long userId: projectUserDTO.getUserId()) {
            ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(userId,projectUserDTO.getProjectId());
            if (projectUsers!=null){
                projectUsers.setStatus(0);
                projectUsers.setUpdateDate(new Date());
                projectUsersRepository.save(projectUsers);
            }
        }
        return "Xóa thành công";
    }

    @Override
    public String addUserToProject(ProjectUserDTO projectUserDTO) {
        Assert.notNull(projectUserDTO.getProjectId(),"Mã dự án không được để trống ");
        Assert.notNull(projectUserDTO.getUserId(),"Mã nhân viên không được để trống ");
        for (Long userId: projectUserDTO.getUserId()) {
            ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(userId,projectUserDTO.getProjectId());
            Assert.isTrue(projectUsers==null,"Nhân viên đã trong dự án này");
            ProjectUsers p=  ProjectUsers.builder()
                                .status(1)
                                .createDate(new Date())
                                .projectId(projectUserDTO.getProjectId())
                                .userId(userId).build();
                projectUsersRepository.save(p);
        }
        return "Add User thành công";
    }

    @Override
    public ObjectPaging getProjectByMember(String token, ObjectPaging objectPaging) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(),Sort.by("priorityId").ascending()
                .and(Sort.by("status").descending())
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
