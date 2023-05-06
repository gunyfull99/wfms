package com.wfms.service.impl;

import com.wfms.entity.TaskUsers;
import com.wfms.repository.TaskUsersRepository;
import com.wfms.service.TaskUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

@Service
public class TaskUsersServiceImpl implements TaskUsersService {
    @Autowired
    private TaskUsersRepository taskUsersRepository;
    @Override
    public TaskUsers createTaskUser(TaskUsers taskUsers) {
        Assert.notNull(taskUsers.getTaskId()," TaskId không được để trống");
        Assert.notNull(taskUsers.getUserId()," UserId không được để trống");
        Assert.notNull(taskUsers.getIsResponsible()," IsResponsible không được để trống");
       // Assert.notNull(taskUsers.getIsTesterResponsible()," IsTesterResponsible không được để trống");
        taskUsers.setTaskUserId(null);
        taskUsers.setCreateDate(new Date());
        return taskUsersRepository.save(taskUsers);
    }
}
