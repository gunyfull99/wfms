package com.wfms.service.impl;

import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.WorkFlowDTO;
import com.wfms.entity.ProjectUsers;
import com.wfms.entity.Projects;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.service.ProjectService;
import com.wfms.service.WorkFlowService;
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
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    @Autowired
    private WorkFlowService workFlowService;

    @Override
    public List<Projects> findAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public Page<Projects> findProjectWithPageable(int total, int page) {
        Pageable pageable = PageRequest.of(page,total, Sort.Direction.DESC);
        return projectRepository.findAll(pageable);
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        Assert.isTrue(projectDTO.getProjectTypeId()!=null,"Loại dự án không được để trống");
        Assert.isTrue(projectDTO.getProjectId()!=null,"ID dự án không được để trống");
        Assert.isTrue(DataUtils.notNull(projectDTO.getUserId()),"Nhân viên dự án không được để trống");
        Assert.isTrue(Objects.nonNull(projectDTO.getLead()),"Người quản lý dự án không được để trống");
        Projects projects = projectRepository.getById(projectDTO.getProjectId());
        Assert.notNull(projects,"Không tìm thấy project với ID "+projectDTO.getProjectId());
        BeanUtils.copyProperties(projectDTO,projects);
        Long projectId = projectRepository.save(projects).getProjectId();
//        for (Long userId: projectDTO.getUserId()) {
//            ProjectUsers projectUsers = ProjectUsers.builder().projectId(projectId).userId(userId).build();
//            projectUsersRepository.save(projectUsers);
//        }
        return projectDTO;
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Assert.isTrue(projectDTO.getProjectTypeId()!=null,"Loại dự án không được để trống");
        Assert.isTrue(DataUtils.notNull(projectDTO.getUserId()),"Nhân viên dự án không được để trống");
        Assert.isTrue(Objects.nonNull(projectDTO.getLead()),"Người quản lý dự án không được để trống");
        Projects projects = new Projects();
        BeanUtils.copyProperties(projectDTO,projects);
        projects.setProjectId(null);
        projects.setStatus(1);
        projects.setCreateDate(new Date());
        Projects p = projectRepository.save(projects);
        for (Long userId: projectDTO.getUserId()) {
            ProjectUsers projectUsers = ProjectUsers.builder().projectId(p.getProjectId()).userId(userId).build();
            projectUsersRepository.save(projectUsers);
        }
        workFlowService.createWorkFlow(new WorkFlowDTO().builder().projectId(p.getProjectId()).build());
        BeanUtils.copyProperties(p,projectDTO);
        return projectDTO;
    }
}
