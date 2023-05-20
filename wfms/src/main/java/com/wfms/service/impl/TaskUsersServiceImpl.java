package com.wfms.service.impl;

import com.wfms.config.Const;
import com.wfms.entity.TaskUsers;
import com.wfms.repository.TaskUsersRepository;
import com.wfms.service.TaskUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


 import java.time.LocalDateTime; 

@Service
public class TaskUsersServiceImpl implements TaskUsersService {
    @Autowired
    private TaskUsersRepository taskUsersRepository;
    @Override
    public TaskUsers createTaskUser(TaskUsers taskUsers) {
        Assert.notNull(taskUsers.getTaskId(), Const.responseError.taskId_null);
        Assert.notNull(taskUsers.getUserId(),Const.responseError.userId_null);
        Assert.notNull(taskUsers.getIsResponsible()," IsResponsible must not be null");
       // Assert.notNull(taskUsers.getIsTesterResponsible()," IsTesterResponsible không được để trống");
        taskUsers.setTaskUserId(null);
        taskUsers.setCreateDate(LocalDateTime.now());
        return taskUsersRepository.save(taskUsers);
    }
}
