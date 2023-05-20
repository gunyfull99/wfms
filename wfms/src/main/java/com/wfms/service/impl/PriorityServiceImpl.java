package com.wfms.service.impl;

import com.wfms.Dto.PriorityDTO;
import com.wfms.entity.Priority;
import com.wfms.repository.PriorityRepository;
import com.wfms.service.PriorityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


 import java.time.LocalDateTime; 
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
    public Priority createPriority(PriorityDTO priority) {
        Assert.isTrue(Objects.nonNull(priority.getPriorityName()),"PriorityName must not be null");
        Priority p = new Priority();
        BeanUtils.copyProperties(priority,p);
        p.setPriorityId(null);
        p.setStatus(1);
        p.setCreateDate(LocalDateTime.now());
        p = priorityRepository.save(p);
        return p;
    }
}
