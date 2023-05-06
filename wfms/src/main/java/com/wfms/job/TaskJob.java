package com.wfms.job;

import com.wfms.entity.Task;
import com.wfms.job.thread.SendNotificationTask;
import com.wfms.job.thread.UpdateTask;
import com.wfms.repository.TaskRepository;
import com.wfms.utils.Constants;
import com.wfms.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class TaskJob {
    @Autowired
    private TaskRepository taskRepository;
    @Scheduled(cron = "* */10 * * * *")
    public void checkDeadlineProjectAndUpdatePriority(){
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Start job check deadline task <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        List<Task>exTask=new ArrayList<>();
        List<Task>highTask=new ArrayList<>();
        List<Task>moderTask=new ArrayList<>();
        List<Task> tasks = taskRepository.getListTaskActive();
        if(DataUtils.listNotNullOrEmpty(tasks)){
        tasks.forEach(o ->{
            Assert.notNull(o.getDeadLine(),"Deadline task không được để trống");
            Assert.notNull(o.getApproveDate(),"ApproveDate task không được để trống");
            Assert.notNull(o.getPriority(),"Mức độ ưu tiên task không được để trống");
            if(Constants.HIGH.equals(o.getPriority().getPriorityId())){
                Date d= DataUtils.getPeriodDate(o.getApproveDate(),o.getDeadLine(), Constants.PERIOD_1);
                if(new Date().after(d)){
                    exTask.add(o);
                }
            }else if(Constants.MODERATE.equals(o.getPriority().getPriorityId())){
                Date d= DataUtils.getPeriodDate(o.getApproveDate(),o.getDeadLine(), Constants.PERIOD_2);
                if(new Date().after(d)){
                    highTask.add(o);
                }
            }else if(Constants.LOW.equals(o.getPriority().getPriorityId())){
                Date d= DataUtils.getPeriodDate(o.getApproveDate(),o.getDeadLine(), Constants.PERIOD_3);
                if(new Date().after(d)){
                    moderTask.add(o);
                }
            }
        });
        }

        UpdateTask updateTask = UpdateTask.builder()
                    .listExtremeTask(exTask)
                    .listHighTaskt(highTask)
                    .listModerateTask(moderTask).build();
        Thread updateTaskThread = new Thread(updateTask);
        updateTaskThread.start();
        SendNotificationTask sendNotificationTask = SendNotificationTask.builder()
                                                        .listExtremeTask(exTask)
                                                        .listHighTask(highTask)
                                                        .listModerateTask(moderTask).build();
        Thread sendNotificationThread = new Thread(sendNotificationTask);
        updateTaskThread.start();
        sendNotificationThread.start();
    }
}
