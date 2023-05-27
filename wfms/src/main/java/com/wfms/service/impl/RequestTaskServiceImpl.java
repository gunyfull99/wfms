package com.wfms.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.*;
import com.wfms.config.Const;
import com.wfms.entity.Notification;
import com.wfms.entity.RequestTask;
import com.wfms.entity.TaskUsers;
import com.wfms.entity.Users;
import com.wfms.repository.NotificationRepository;
import com.wfms.repository.RequestTaskRepository;
import com.wfms.service.FireBaseService;
import com.wfms.service.RequestTaskService;
import com.wfms.service.TaskService;
import com.wfms.service.UsersService;
import com.wfms.utils.DataUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.util.ArrayList;
 import java.time.LocalDateTime; 
import java.util.List;

@Service
@Log4j2
public class RequestTaskServiceImpl implements RequestTaskService {

    @Autowired
    private RequestTaskRepository requestTaskRepository;

    @Autowired
    private TaskService taskService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Override
    public String approveRejectRequest(RequestTask requestTaskD, Integer status) throws FirebaseMessagingException {
        Assert.notNull(requestTaskD,"RequestTaskId must not be null");
        List<RequestTask> requestTask=requestTaskRepository.getRequestTaskInStatus(requestTaskD.getRequestTaskId(),List.of(0,1,2));
        Assert.isTrue(DataUtils.listNotNullOrEmpty(requestTask) ,"Not found request task");
        TaskDTO t= taskService.getDetailTaskById(requestTask.get(0).getTaskId());
        Assert.notNull(t , Const.responseError.task_notFound +requestTask.get(0).getTaskId());
        Assert.isTrue(requestTask.get(0).getStatus().equals(1),"Request status not in pending");
        Assert.isTrue(!(requestTask.get(0).getStatus().equals(status)),"The request is already in this status");
        List<Notification> notificationEntities = new ArrayList<>();
        requestTask.get(0).setStatus(status);
        if(status.equals(2)){
            Assert.notNull(requestTaskD.getMain(),"Main must not be null");
            List<TaskUsers> listTaskUser=new ArrayList<>();
            Users users =usersService.findById(requestTask.get(0).getUserId());
            TaskUsers taskUsers=TaskUsers.builder()
                    .taskId(requestTask.get(0).getTaskId())
                    .userId(requestTask.get(0).getUserId())
                    .isResponsible(requestTaskD.getMain())
                 //   .isTesterResponsible(users.getJobTitle().equals("TESTER"))
                    .status(2)
                    .createDate(LocalDateTime.now())
                    .build();
            listTaskUser.add(taskUsers);
            taskService.updateAssignessTask(listTaskUser);
            requestTaskRepository.save( requestTask.get(0));
            notificationEntities.add(Notification.builder()
                    .taskId(requestTask.get(0).getTaskId())
                    .userId(requestTask.get(0).getUserId())
                    .title("Approve request join task  "+t.getCode())
                    .description("You have been approve to the task "+t.getCode())
                    .status(1)
                    .timeRecive(LocalDateTime.now())
                    .createDate(LocalDateTime.now())
                    .build());
            MessageDto messageDtoList =   MessageDto.builder().userId(List.of(requestTask.get(0).getUserId()))
                    .notification(NotificationDto.builder().taskId(t.getTaskId()).title("Approve request join task  "+t.getCode()).body("You have been approve to the task "+t.getCode()).build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
            notificationRepository.saveAll(notificationEntities);

            return "Approve successfuly";
        }
        Assert.notNull(requestTaskD.getReason(),"Reason reject must not be null");
        requestTask.get(0).setReason(requestTaskD.getReason());
        requestTaskRepository.save( requestTask.get(0));
        notificationEntities.add(Notification.builder()
                .taskId(requestTask.get(0).getTaskId())
                .userId(requestTask.get(0).getUserId())
                .title("Reject request join task  "+t.getCode())
                .description("You have been reject to the task "+t.getCode())
                .status(1)
                .timeRecive(LocalDateTime.now())
                .createDate(LocalDateTime.now())
                .build());
        MessageDto messageDtoList =   MessageDto.builder().userId(List.of(requestTask.get(0).getUserId()))
                .notification(NotificationDto.builder().title("Reject request join task  "+t.getCode()).body("You have been reject to the task "+t.getCode()).build()).build();
        fireBaseService.sendManyNotification(messageDtoList);
        notificationRepository.saveAll(notificationEntities);
        return "Reject successfuly";
    }

    @Override
    public ObjectPaging searchRequestTask(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("requestTaskId").descending());
        List<Long>listTask=null;
        if(DataUtils.listNotNullOrEmpty(objectPaging.getListTaskId())){
            listTask=objectPaging.getListTaskId();
        }
        Page<RequestTask> list =requestTaskRepository.searchRequestTask(objectPaging.getStatus(),objectPaging.getUserId(),listTask,pageable);
        List<RequestTaskDto> taskDTOList=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(list.getContent())){
        list.getContent().forEach(o->{
            RequestTaskDto requestTaskDto=new RequestTaskDto();
            UsersDto u =usersService.getUserById(o.getUserId());
            TaskDTO t= taskService.getDetailTaskById(o.getTaskId());
            BeanUtils.copyProperties(o,requestTaskDto);
            requestTaskDto.setTaskId(t);
            requestTaskDto.setUserId(u);
            taskDTOList.add(requestTaskDto);
        });
        }

        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(taskDTOList).build();
    }
}
