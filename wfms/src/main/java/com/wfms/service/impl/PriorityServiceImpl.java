package com.wfms.service.impl;

import com.wfms.entity.Priority;
import com.wfms.repository.PriorityRepository;
import com.wfms.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PriorityServiceImpl implements PriorityService {
    @Autowired
    private PriorityRepository priorityRepository;
    @Override
    public List<Priority> findAll() {
        return priorityRepository.findAll();
    }

    @Override
    public Priority createPriority(Priority priority) {
        Assert.isTrue(Objects.nonNull(priority.getPriorityName()),"Tên mức độ không được để trống");
        priority.setPriorityId(null);
        priority.setStatus(1);
        priority.setCreateDate(new Date());
        return priorityRepository.save(priority);
    }
}
