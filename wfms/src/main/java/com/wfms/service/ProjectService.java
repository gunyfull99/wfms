package com.wfms.service;

import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.ProjectUserDTO;
import com.wfms.entity.Projects;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> findAllProject();
    List<ProjectDTO> findAllProjectByLead(String token);
    ProjectDTO getDetailProject(Long projectId);
    Page<Projects> findProjectWithPageable(int total, int page);
    Projects updateProject(Projects project);
    ProjectDTO createProject(ProjectDTO projectDTO);
    String removeUserFromProject(ProjectUserDTO projectUserDTO);
    String addUserToProject(ProjectUserDTO projectUserDTO);

}
