package com.wfms.service;

import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.ProjectUserDTO;
import com.wfms.entity.Projects;
import org.springframework.data.domain.Page;

public interface ProjectService {
    ObjectPaging findAllProject(ObjectPaging objectPaging);
    ObjectPaging findAllProjectByLead(String token,ObjectPaging objectPaging);
    ProjectDTO getDetailProject(Long projectId);
    Page<Projects> findProjectWithPageable(int total, int page);
    Projects updateProject(Projects project);
    ProjectDTO createProject(ProjectDTO projectDTO);
    String removeUserFromProject(ProjectUserDTO projectUserDTO);
    String addUserToProject(ProjectUserDTO projectUserDTO);
    ObjectPaging getProjectByMember(String token,ObjectPaging objectPaging);
}
