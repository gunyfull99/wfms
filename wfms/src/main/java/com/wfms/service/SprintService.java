package com.wfms.service;

import com.wfms.entity.Sprint;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SprintService {
    List<Sprint> findAll();
    Page<Sprint> findAllWithPage(int total,int page);
    List<Sprint> findSprintByProjectId(Long projectId);
}
