//package com.wfms.service.impl;
//
//import com.wfms.Dto.ProjectDTO;
//import com.wfms.Dto.ProjectTypeDTO;
//import com.wfms.entity.ProjectType;
//import com.wfms.entity.ProjectUsers;
//import com.wfms.entity.Projects;
//import com.wfms.repository.ProjectRepository;
//import com.wfms.repository.ProjectTypeRepository;
//import com.wfms.repository.ProjectUsersRepository;
//import com.wfms.service.ProjectService;
//import com.wfms.service.ProjectTypeService;
//import com.wfms.utils.DataUtils;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Assert;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class ProjectTypeServiceImpl implements ProjectTypeService {
//    @Autowired
//    private ProjectTypeRepository projectTypeRepository;
//
//    @Override
//    public List<ProjectType> findAllProjectType() {
//        return projectTypeRepository.findAll();
//    }
//
//    @Override
//    public Page<ProjectType> findProjectTypeWithPageable(int total, int page) {
//        return null;
//    }
//
//    @Override
//    public ProjectType updateProjectType(ProjectType projectTypeDTO) {
//        Assert.isTrue(Objects.nonNull(projectTypeDTO.getProjectTypeName()),"Tên loại dự án không được để trống");
//        Assert.isTrue(Objects.nonNull(projectTypeDTO.getProjectTypeId()),"ID loại dự án không được để trống");
//        ProjectType projectType =projectTypeRepository.getById(projectTypeDTO.getProjectTypeId());
//        Assert.notNull(projectType,"Không tìm thấy ID loại dự án");
//        BeanUtils.copyProperties(projectTypeDTO,projectType);
//        projectType.setUpdateDate(new Date());
//        BeanUtils.copyProperties(projectTypeRepository.save(projectType),projectTypeDTO);
//        return projectTypeDTO;
//    }
//    @Override
//    public ProjectTypeDTO createProjectType(ProjectTypeDTO projectTypeDTO) {
//        Assert.isTrue(Objects.nonNull(projectTypeDTO.getProjectTypeName()),"Tên loại dự án không được để trống");
//        ProjectType projectType = new ProjectType();
//        BeanUtils.copyProperties(projectTypeDTO,projectType);
//        projectType.setProjectTypeId(null);
//        projectType.setStatus(1);
//        projectType.setUpdateDate(new Date());
//        BeanUtils.copyProperties(projectTypeRepository.save(projectType),projectTypeDTO);
//        return projectTypeDTO;
//    }
//
//}
