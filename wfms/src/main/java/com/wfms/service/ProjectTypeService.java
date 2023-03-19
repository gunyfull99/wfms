package com.wfms.service;

import com.wfms.Dto.ProjectTypeDTO;
import com.wfms.entity.ProjectType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectTypeService {
    List<ProjectType> findAllProjectType();
    Page<ProjectType> findProjectTypeWithPageable(int total, int page);
    ProjectType updateProjectType(ProjectType projectTypeDTO);
    ProjectTypeDTO createProjectType(ProjectTypeDTO projectTypeDTO);
}
