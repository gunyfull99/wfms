package com.wfms.service;

import com.wfms.Dto.ProjectDTO;
import com.wfms.entity.Projects;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {
    List<Projects> findAll();
    Page<Projects> findProjectWithPageable(int total, int page);

    ProjectDTO createProject(ProjectDTO projectDTO);
}
