package com.wfms.service;

import com.wfms.Dto.SprintDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.Sprint;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SprintService {
    List<Sprint> findAll();
    Page<Sprint> findAllWithPage(int total,int page);
    List<SprintDTO> findSprintByProjectId(Long projectId);
    ObjectPaging searchSprint(ObjectPaging objectPaging);
    SprintDTO createSprint(SprintDTO sprintDTO);
    SprintDTO getDetailSprint(Long sprintId);
    Sprint updateSprint(Sprint sprintDTO);
    String completeSprint(Long sprintId);
    String startSprint(Long sprintId);

}
