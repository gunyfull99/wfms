package com.wfms.service.impl;

import com.wfms.entity.TaskTypes;
import com.wfms.repository.TaskTypeRepository;
import com.wfms.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


 import java.time.LocalDateTime; 
import java.util.List;
import java.util.Objects;
@Service
public class TaskTypeServiceImpl implements TaskTypeService {

    @Autowired
    private TaskTypeRepository taskTypeRepository;

    @Override
    public TaskTypes createTaskType(TaskTypes taskTypes) {
        Assert.isTrue(Objects.nonNull(taskTypes.getTaskTypeName()),"TaskType name must not be null");
        taskTypes.setTaskTypeId(null);
        taskTypes.setStatus(1);
        taskTypes.setCreateDate(LocalDateTime.now());
        return taskTypeRepository.save(taskTypes);
    }

    @Override
    public List<TaskTypes> listTaskType() {
        return taskTypeRepository.findAll();
    }
}
