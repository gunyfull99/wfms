package com.wfms.service;

import com.google.firebase.messaging.FirebaseMessagingException;
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
    Projects startEndProject(Long projectId,Integer status);
    ProjectDTO createProject(ProjectDTO projectDTO) throws FirebaseMessagingException;
    String removeUserFromProject(ProjectUserDTO projectUserDTO) throws FirebaseMessagingException;
    String addUserToProject(ProjectUserDTO projectUserDTO) throws FirebaseMessagingException;
    ObjectPaging getProjectByMember(String token,ObjectPaging objectPaging);
}
