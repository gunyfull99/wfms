package com.wfms.service.impl;

import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.RequestTaskDto;
import com.wfms.Dto.TaskDTO;
import com.wfms.Dto.UsersDto;
import com.wfms.entity.RequestTask;
import com.wfms.entity.TaskUsers;
import com.wfms.entity.Users;
import com.wfms.repository.RequestTaskRepository;
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
    @Override
    public String approveRejectRequest(Long requestTaskId, Integer status) {
        Assert.notNull(requestTaskId,"RequestTaskId không được để trống");
        List<RequestTask> requestTask=requestTaskRepository.getRequestTaskInStatus(requestTaskId,List.of(status));
        Assert.isTrue(DataUtils.listNotNullOrEmpty(requestTask) ,"Không tìm thấy yêu cầu");
        Assert.isTrue(requestTask.get(0).getStatus().equals(1),"Request không phải trạng thái pending");
        Assert.isTrue(!(requestTask.get(0).getStatus().equals(status)),"Request đã ở trạng thái này rồi");
        requestTask.get(0).setStatus(status);
        if(status.equals(2)){
            List<TaskUsers> listTaskUser=new ArrayList<>();
            Users users =usersService.findById(requestTask.get(0).getUserId());
            TaskUsers taskUsers=TaskUsers.builder()
                    .taskId(requestTask.get(0).getTaskId())
                    .userId(requestTask.get(0).getUserId())
                    .isResponsible(users.getJobTitle().equals("DEV"))
                    .isResponsible(users.getJobTitle().equals("TESTER"))
                    .status(2)
                    .createDate(new Date())
                    .build();
            listTaskUser.add(taskUsers);
            taskService.updateAssignessTask(listTaskUser);
            requestTaskRepository.save( requestTask.get(0));
            return "Approve successfuly";
        }
        requestTaskRepository.save( requestTask.get(0));
        return "Reject successfuly";
    }

    @Override
    public ObjectPaging searchRequestTask(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("requestTaskId").descending());
        Page<RequestTask> list =requestTaskRepository.searchRequestTask(objectPaging.getStatus(),pageable);
        List<RequestTaskDto> taskDTOList=new ArrayList<>();
        list.getContent().forEach(o->{
            RequestTaskDto requestTaskDto=new RequestTaskDto();
            UsersDto u =usersService.getUserById(o.getUserId());
            TaskDTO t= taskService.getDetailTaskById(o.getTaskId());
            BeanUtils.copyProperties(o,requestTaskDto);
            requestTaskDto.setTaskId(t);
            requestTaskDto.setUserId(u);
            taskDTOList.add(requestTaskDto);
        });
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(taskDTOList).build();
    }
}
