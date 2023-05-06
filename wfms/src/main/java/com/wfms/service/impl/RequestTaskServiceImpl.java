package com.wfms.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.*;
import com.wfms.entity.News;
import com.wfms.entity.RequestTask;
import com.wfms.entity.TaskUsers;
import com.wfms.entity.Users;
import com.wfms.repository.NewsRepository;
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
import java.util.Date;
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
    private NewsRepository newsRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Override
    public String approveRejectRequest(RequestTask requestTaskD, Integer status) throws FirebaseMessagingException {
        Assert.notNull(requestTaskD,"RequestTaskId không được để trống");
        List<RequestTask> requestTask=requestTaskRepository.getRequestTaskInStatus(requestTaskD.getRequestTaskId(),List.of(status));
        Assert.isTrue(DataUtils.listNotNullOrEmpty(requestTask) ,"Không tìm thấy yêu cầu");
        TaskDTO t= taskService.getDetailTaskById(requestTask.get(0).getTaskId());
        Assert.notNull(t ,"Không tìm thấy taskId "+requestTask.get(0).getTaskId());
        Assert.isTrue(requestTask.get(0).getStatus().equals(1),"Request không phải trạng thái pending");
        Assert.isTrue(!(requestTask.get(0).getStatus().equals(status)),"Request đã ở trạng thái này rồi");
        List<News> newsEntitys = new ArrayList<>();
        requestTask.get(0).setStatus(status);
        if(status.equals(2)){
            List<TaskUsers> listTaskUser=new ArrayList<>();
            Users users =usersService.findById(requestTask.get(0).getUserId());
            TaskUsers taskUsers=TaskUsers.builder()
                    .taskId(requestTask.get(0).getTaskId())
                    .userId(requestTask.get(0).getUserId())
                    .isResponsible(true)
                 //   .isTesterResponsible(users.getJobTitle().equals("TESTER"))
                    .status(2)
                    .createDate(new Date())
                    .build();
            listTaskUser.add(taskUsers);
            taskService.updateAssignessTask(listTaskUser);
            requestTaskRepository.save( requestTask.get(0));
            newsEntitys.add(News.builder()
                    .taskId(requestTask.get(0).getTaskId())
                    .userId(requestTask.get(0).getUserId())
                    .title("Approve request join task  "+t.getCode())
                    .description("You have been approve to the task "+t.getCode())
                    .status(1)
                    .timeRecive(new Date())
                    .createDate(new Date())
                    .build());
            MessageDto messageDtoList =   MessageDto.builder().userId(List.of(requestTask.get(0).getUserId()))
                    .notification(NotificationDto.builder().title("Approve request join task  "+t.getCode()).body("You have been approve to the task "+t.getCode()).build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
            newsRepository.saveAll(newsEntitys);

            return "Approve successfuly";
        }
        Assert.notNull(requestTaskD.getReason(),"Reason reject không được để trống");
        requestTask.get(0).setReason(requestTaskD.getReason());
        requestTaskRepository.save( requestTask.get(0));
        newsEntitys.add(News.builder()
                .taskId(requestTask.get(0).getTaskId())
                .userId(requestTask.get(0).getUserId())
                .title("Reject request join task  "+t.getCode())
                .description("You have been reject to the task "+t.getCode())
                .status(1)
                .timeRecive(new Date())
                .createDate(new Date())
                .build());
        MessageDto messageDtoList =   MessageDto.builder().userId(List.of(requestTask.get(0).getUserId()))
                .notification(NotificationDto.builder().title("Reject request join task  "+t.getCode()).body("You have been reject to the task "+t.getCode()).build()).build();
        fireBaseService.sendManyNotification(messageDtoList);
        newsRepository.saveAll(newsEntitys);
        return "Reject successfuly";
    }

    @Override
    public ObjectPaging searchRequestTask(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("requestTaskId").descending());
        Page<RequestTask> list =requestTaskRepository.searchRequestTask(objectPaging.getStatus(),pageable);
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
