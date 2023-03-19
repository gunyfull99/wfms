package com.wfms.service;

import com.wfms.Dto.SprintDTO;
import com.wfms.entity.Sprint;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SprintService {
    List<Sprint> findAll();
    Page<Sprint> findAllWithPage(int total,int page);
    List<SprintDTO> findSprintByProjectId(Long projectId);
    SprintDTO createSprint(SprintDTO sprintDTO);
    SprintDTO getDetailSprint(Long sprintId);
    Sprint updateSprint(Sprint sprintDTO);

}
