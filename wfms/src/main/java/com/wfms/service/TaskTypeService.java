package com.wfms.service;


import com.wfms.entity.TaskTypes;

import java.util.List;

public interface TaskTypeService {
    TaskTypes createTaskType(TaskTypes taskTypes);
    List<TaskTypes> listTaskType();
}
