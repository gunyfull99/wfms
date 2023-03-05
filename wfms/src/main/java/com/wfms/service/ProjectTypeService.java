package com.wfms.service;

import com.wfms.Dto.ProjectTypeDTO;
import com.wfms.entity.ProjectType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectTypeService {
    List<ProjectType> findAllProject();
    Page<ProjectType> findProjectWithPageable(int total, int page);
    ProjectTypeDTO updateProjectType(ProjectTypeDTO projectTypeDTO);
    ProjectTypeDTO createProjectType(ProjectTypeDTO projectTypeDTO);
}
