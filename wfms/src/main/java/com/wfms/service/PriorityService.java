package com.wfms.service;

import com.wfms.entity.Priority;
import com.wfms.entity.Sprint;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PriorityService {
    List<Priority> findAll();
    Priority createPriority(Priority priority);

}
