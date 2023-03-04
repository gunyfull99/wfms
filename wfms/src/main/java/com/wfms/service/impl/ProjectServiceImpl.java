package com.wfms.service.impl;

import com.wfms.Dto.ProjectDTO;
import com.wfms.entity.ProjectUsers;
import com.wfms.entity.Projects;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.service.ProjectService;
import com.wfms.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    @Override
    public List<Projects> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Page<Projects> findProjectWithPageable(int total, int page) {
        Pageable pageable = PageRequest.of(page,total, Sort.Direction.DESC);
        return projectRepository.findAll(pageable);
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Assert.isTrue(projectDTO.getProjectType()!=null,"Loại dự án không được để trống");
        Assert.isTrue(DataUtils.notNull(projectDTO.getUserId()),"Nhân viên dự án không được để trống");
        Assert.isTrue(DataUtils.notNullOrEmpty(projectDTO.getLead()),"Người quản lý dự án không được để trống");
        Projects projects = new Projects();
        BeanUtils.copyProperties(projects,projectDTO);
        Long projectId = projectRepository.save(projects).getProjectId();
        for (Long userId: projectDTO.getUserId()) {
            ProjectUsers projectUsers = ProjectUsers.builder().projectId(projectId).userId(userId).build();
            projectUsersRepository.save(projectUsers);
        }
        return projectDTO;
    }
}
