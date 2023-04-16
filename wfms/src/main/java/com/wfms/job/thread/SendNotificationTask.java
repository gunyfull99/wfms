package com.wfms.job.thread;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.entity.DeviceUsers;
import com.wfms.entity.News;
import com.wfms.entity.TaskUsers;
import com.wfms.entity.Task;
import com.wfms.repository.DevicesUsersRepository;
import com.wfms.repository.NewsRepository;
import com.wfms.repository.TaskUsersRepository;
import com.wfms.service.FireBaseService;
import com.wfms.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class SendNotificationTask extends Thread{
    @Autowired
    private TaskUsersRepository taskUsersRepository;
    @Autowired
    private DevicesUsersRepository devicesUsersRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Autowired
    private NewsRepository newsRepository;
    private List<Task> listTaskOneWeek;
    private List<Task> listProject3Day;
    private List<Task> listProject1Day;
    private List<Task> listProject5Day;

    @Override
    public void run(){
        try {
            this.sendNotification();
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendNotification() throws FirebaseMessagingException {
        List<News> newsEntitys = new ArrayList<>();
        if (DataUtils.notNull(listTaskOneWeek)){
        List<Long> userIds=new ArrayList<>();
            for (Task task : this.listTaskOneWeek) {
                TaskUsers taskUsers =  taskUsersRepository.findTaskUsersByTaskIdAndIsResponsible(task.getTaskId(),true);
                List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(taskUsers.getUserId());
                newsEntitys.add(News.builder()
                        .taskId(task.getTaskId())
                        .userId(taskUsers.getUserId())
                        .title("Deadline in one week")
                        .description("Deadline in")
                        .status(1)
                        .timeRecive(new Date())
                        .createDate(new Date())
                        .build());
                deviceUsers.forEach(i->{
                    userIds.add(i.getUserId());
                });
            }
            MessageDto messageDtoList =   MessageDto.builder().userId(userIds)
                    .notification(NotificationDto.builder().title("Deadline in one week").body("Deadline in").build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
        }
        if (DataUtils.notNull(listProject3Day)){
            List<Long> userIds=new ArrayList<>();
            for (Task task : this.listProject3Day) {
                TaskUsers taskUsers =  taskUsersRepository.findTaskUsersByTaskIdAndIsResponsible(task.getTaskId(),true);
                List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(taskUsers.getUserId());
                newsEntitys.add(News.builder()
                        .taskId(task.getTaskId())
                        .userId(taskUsers.getUserId())
                        .title("Deadline in 3 day")
                        .description("Deadline in")
                        .status(1)
                        .timeRecive(new Date())
                        .createDate(new Date())
                        .build());
                deviceUsers.forEach(i->{
                    userIds.add(i.getUserId());
                });
            }
            MessageDto messageDtoList =   MessageDto.builder().userId(userIds)
                    .notification(NotificationDto.builder().title("Deadline in 3 day").body("Deadline in").build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
        }
        if (DataUtils.notNull(listProject1Day)){
            List<Long> userIds=new ArrayList<>();
            for (Task task : this.listProject1Day) {
                TaskUsers taskUsers =  taskUsersRepository.findTaskUsersByTaskIdAndIsResponsible(task.getTaskId(),true);
                List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(taskUsers.getUserId());
                newsEntitys.add(News.builder()
                        .taskId(task.getTaskId())
                        .userId(taskUsers.getUserId())
                        .title("Deadline in one day")
                        .description("Deadline in")
                        .status(1)
                        .timeRecive(new Date())
                        .createDate(new Date())
                        .build());
                deviceUsers.forEach(i->{
                    userIds.add(i.getUserId());
                });
            }
            MessageDto messageDtoList =   MessageDto.builder().userId(userIds)
                    .notification(NotificationDto.builder().title("Deadline in one day").body("Deadline in").build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
        }
        if (DataUtils.notNull(listProject5Day)){
            List<Long> userIds=new ArrayList<>();
            for (Task task : this.listProject5Day) {
                TaskUsers taskUsers =  taskUsersRepository.findTaskUsersByTaskIdAndIsResponsible(task.getTaskId(),true);
                List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(taskUsers.getUserId());
                newsEntitys.add(News.builder()
                        .taskId(task.getTaskId())
                        .userId(taskUsers.getUserId())
                        .title("Deadline in five day")
                        .description("Deadline in")
                        .status(1)
                        .timeRecive(new Date())
                        .createDate(new Date())
                        .build());
                deviceUsers.forEach(i->{
                    userIds.add(i.getUserId());
                });
            }
            MessageDto messageDtoList =   MessageDto.builder().userId(userIds)
                    .notification(NotificationDto.builder().title("Deadline in five day").body("Deadline in").build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
        }
        newsRepository.saveAll(newsEntitys);
    }
}
