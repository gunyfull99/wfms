package com.wfms.job;

import com.wfms.entity.Task;
import com.wfms.job.thread.SendNotificationTask;
import com.wfms.job.thread.UpdateTask;
import com.wfms.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class TaskJob {
    @Autowired
    private TaskRepository taskRepository;
    //@Scheduled(cron = "8 * * * * *")
    public void checkDeadlineProjectAndUpdatePriority(){
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Start job check deadline task <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarEndDateOneMonth =  Calendar.getInstance();
        Date startDate = new Date();
        calendarEndDateOneMonth.setTime(startDate);
        System.out.println("Ngày ban đầu : " + dateFormat.format(startDate));
        calendarEndDateOneMonth.add(Calendar.DATE, 7);
        System.out.println("Deadline 7 ngày: " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Task> taskDeadlineSevenDay = taskRepository.getTaskByDeadline(calendarEndDateOneMonth.getTime());
        calendarEndDateOneMonth.add(Calendar.DATE,-2);
        System.out.println("Deadline 5 ngày : " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Task> taskDeadlineFiveDay =  taskRepository.getTaskByDeadline(calendarEndDateOneMonth.getTime());
        calendarEndDateOneMonth.add(Calendar.DATE,-2);
        System.out.println("Deadline 3 ngày : " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Task> taskDeadlineThreeDay =  taskRepository.getTaskByDeadline(calendarEndDateOneMonth.getTime());
        calendarEndDateOneMonth.add(Calendar.DATE,-2);
        System.out.println("Deadline 1 ngày : " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Task> taskDeadlineOneDay =  taskRepository.getTaskByDeadline(calendarEndDateOneMonth.getTime());

        UpdateTask updateTask = UpdateTask.builder()
                    .listTaskOneDay(taskDeadlineOneDay)
                    .listTaskThreeDay(taskDeadlineThreeDay)
                    .listTaskFiveDay(taskDeadlineFiveDay)
                    .listTaskSevenDay(taskDeadlineSevenDay).build();
        Thread updateTaskThread = new Thread(updateTask);
        updateTaskThread.start();
        SendNotificationTask sendNotificationTask = SendNotificationTask.builder()
                                                        .listTaskOneWeek(taskDeadlineSevenDay)
                                                        .listProject1Day(taskDeadlineOneDay)
                                                        .listProject3Day(taskDeadlineThreeDay)
                                                        .listProject5Day(taskDeadlineFiveDay).build();
        Thread sendNotificationThread = new Thread(sendNotificationTask);
        updateTaskThread.start();
        sendNotificationThread.start();
    }
}
