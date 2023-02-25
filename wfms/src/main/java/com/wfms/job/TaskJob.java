package com.wfms.job;

import com.wfms.config.Const;
import com.wfms.entity.Task;
import com.wfms.job.thread.SendNotificationTask;
import com.wfms.job.thread.UpdateTask;
import com.wfms.repository.DevicesUsersRepository;
import com.wfms.repository.NotificationRepository;
import com.wfms.repository.TaskRepository;
import com.wfms.repository.TaskUsersRepository;
import com.wfms.service.FireBaseService;
import com.wfms.service.TaskService;
import com.wfms.utils.Constants;
import com.wfms.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.util.ArrayList;
 import java.time.LocalDateTime; 
import java.util.List;

@Service
@Slf4j
public class TaskJob {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskUsersRepository taskUsersRepository;
    @Autowired
    private DevicesUsersRepository devicesUsersRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Autowired
    private NotificationRepository notificationRepository;
   @Scheduled(cron = "* */10 * * * *")
    public void checkDeadlineProjectAndUpdatePriority(){
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Start job check deadline task <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        List<Task>exTask=new ArrayList<>();
        List<Task>highTask=new ArrayList<>();
        List<Task>moderTask=new ArrayList<>();
        List<Task> tasks = taskRepository.getListTaskActive();
        if(DataUtils.listNotNullOrEmpty(tasks)){
        tasks.forEach(o ->{
            Assert.notNull(o.getDeadLine(), Const.responseError.deadline_null);
            Assert.notNull(o.getApproveDate(),"ApproveDate task must not be null");
            Assert.notNull(o.getPriority(),Const.responseError.priorityId_null);
            if(Constants.HIGH.equals(o.getPriority().getPriorityId())){
                LocalDateTime d= DataUtils.getPeriodDate(o.getApproveDate(),o.getDeadLine(), Constants.PERIOD_1);
                if(LocalDateTime.now().isAfter(d)){
                    exTask.add(o);
                }
            }else if(Constants.MODERATE.equals(o.getPriority().getPriorityId())){
                LocalDateTime d= DataUtils.getPeriodDate(o.getApproveDate(),o.getDeadLine(), Constants.PERIOD_2);
                if(LocalDateTime.now().isAfter(d)){
                    highTask.add(o);
                }
            }else if(Constants.LOW.equals(o.getPriority().getPriorityId())){
                LocalDateTime d= DataUtils.getPeriodDate(o.getApproveDate(),o.getDeadLine(), Constants.PERIOD_3);
                if(LocalDateTime.now().isAfter(d)){
                    moderTask.add(o);
                }
            }
        });
        }
        UpdateTask updateTask = UpdateTask.builder()
                     .taskRepository(taskRepository)
                    .listExtremeTask(exTask)
                    .listHighTaskt(highTask)
                    .listModerateTask(moderTask).build();
        Thread updateTaskThread = new Thread(updateTask);
        updateTaskThread.start();
        SendNotificationTask sendNotificationTask = SendNotificationTask.builder()
                .taskUsersRepository(taskUsersRepository)
                .notificationRepository(notificationRepository)
                .fireBaseService(fireBaseService)
                .devicesUsersRepository(devicesUsersRepository)
                                                        .listExtremeTask(exTask)
                                                        .listHighTask(highTask)
                                                        .listModerateTask(moderTask).build();
        Thread sendNotificationThread = new Thread(sendNotificationTask);
        sendNotificationThread.start();
    }
}
