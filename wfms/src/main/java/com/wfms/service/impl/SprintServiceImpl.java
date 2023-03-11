package com.wfms.service.impl;

import com.wfms.Dto.SprintDTO;
import com.wfms.entity.Projects;
import com.wfms.entity.Sprint;
import com.wfms.repository.SprintRepository;
import com.wfms.service.SprintService;
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
public class SprintServiceImpl implements SprintService {
    @Autowired
    private SprintRepository sprintRepository;
    @Override
    public List<Sprint> findAll() {
        return sprintRepository.findAll();
    }

    @Override
    public Page<Sprint> findAllWithPage(int total, int page) {
        Pageable pageable = PageRequest.of(page,total, Sort.Direction.DESC);
        return sprintRepository.findAll(pageable);
    }

    @Override
    public List<Sprint> findSprintByProjectId(Long projectId) {
        return sprintRepository.findSprintByProjectId(projectId);
    }

    @Override
    public SprintDTO createSprint(SprintDTO sprintDTO) {
        Assert.isTrue(Objects.nonNull(sprintDTO.getSprintName()),"Tên sprint không được để trống");
        Assert.isTrue(Objects.nonNull(sprintDTO.getGoal()),"Mục tiêu không được để trống");
        Assert.isTrue(Objects.nonNull(sprintDTO.getStartDate()),"Thời gian bắt đầu không được để trống");
        Assert.isTrue(Objects.nonNull(sprintDTO.getEndDate()),"Thời gian kết thúc không được để trống");
        Assert.isTrue(Objects.nonNull(sprintDTO.getProjectId()),"ProjectId không được để trống");
        Sprint sprint = new Sprint();
        BeanUtils.copyProperties(sprintDTO,sprint);
        sprint.setStatus(1);
        sprint.setSprintId(null);
        sprint.setProjects(new Projects().builder().projectId(sprintDTO.getProjectId()).build());
        sprint.setCreateDate(new Date());
        sprintRepository.save(sprint);
        BeanUtils.copyProperties(sprint,sprintDTO);
        return sprintDTO;
    }

    @Override
    public SprintDTO getDetailSprint(Long sprintId) {
        Sprint sprint = sprintRepository.getDetailSprintById(sprintId);
        Assert.isTrue(Objects.nonNull(sprint),"Không tìm thấy sprint");
        SprintDTO sprintDTO= new SprintDTO();
        BeanUtils.copyProperties(sprint,sprintDTO);
        sprintDTO.setProjectId(sprint.getProjects().getProjectId());
        return sprintDTO;
    }

    @Override
    public SprintDTO updateSprint(SprintDTO sprintDTO) {
        Assert.isTrue(Objects.nonNull(sprintDTO.getSprintId()),"ID sprint không được để trống");
        Sprint s = sprintRepository.getById(sprintDTO.getSprintId());
        Assert.notNull(s,"Không tìm thấy sprint");
        Sprint sprint = new Sprint();
        BeanUtils.copyProperties(sprintDTO,sprint);
        sprint.setProjects(s.getProjects());
        sprint.setUpdateDate(new Date());
        sprintRepository.save(sprint);
        BeanUtils.copyProperties(sprint,sprintDTO);
        sprintDTO.setProjectId(sprint.getProjects().getProjectId());
        return sprintDTO;
    }


}
