package com.wfms.service.impl;

import com.wfms.entity.Sprint;
import com.wfms.repository.SprintRepository;
import com.wfms.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
