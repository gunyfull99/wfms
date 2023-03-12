package com.wfms.service;

import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.ProjectUserDTO;
import com.wfms.entity.ProjectUsers;
import com.wfms.entity.Projects;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> findAllProject();
    Page<Projects> findProjectWithPageable(int total, int page);
    ProjectDTO updateProject(ProjectDTO projectDTO);
    ProjectDTO createProject(ProjectDTO projectDTO);
    String removeUserFromProject(ProjectUserDTO projectUserDTO);
}
