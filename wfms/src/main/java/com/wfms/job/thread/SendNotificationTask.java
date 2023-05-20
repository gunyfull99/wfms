package com.wfms.job.thread;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.entity.DeviceUsers;
import com.wfms.entity.Notification;
import com.wfms.entity.Task;
import com.wfms.entity.TaskUsers;
import com.wfms.repository.DevicesUsersRepository;
import com.wfms.repository.NotificationRepository;
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
 import java.time.LocalDateTime; 
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
    private NotificationRepository notificationRepository;
    private List<Task> listExtremeTask;
    private List<Task> listHighTask;
    private List<Task> listModerateTask;
    @Override
    public void run(){
        try {
            this.sendNotification();
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendNotification() throws FirebaseMessagingException {
        List<Notification> notificationEntities = new ArrayList<>();
        if (DataUtils.notNull(listExtremeTask)){
        List<Long> userIds=new ArrayList<>();
            for (Task task : listExtremeTask) {
                TaskUsers taskUsers =  taskUsersRepository.findTaskUsersByTaskIdAndIsResponsible(task.getTaskId(),true);
                List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(taskUsers.getUserId());
                notificationEntities.add(Notification.builder()
                        .taskId(task.getTaskId())
                        .userId(taskUsers.getUserId())
                        .title("Deadline in extreme status")
                        .description("Deadline in")
                        .status(1)
                        .timeRecive(LocalDateTime.now())
                        .createDate(LocalDateTime.now())
                        .build());
                if(DataUtils.listNotNullOrEmpty(deviceUsers)){
                    deviceUsers.forEach(i->{
                        userIds.add(i.getUserId());
                    });
                }
            }
            if(DataUtils.listNotNullOrEmpty(userIds)) {

                MessageDto messageDtoList = MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Deadline in extreme status").body("Deadline in").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }
        }
        if (DataUtils.notNull(listHighTask)) {
            List<Long> userIds = new ArrayList<>();
            for (Task task : listHighTask) {
                TaskUsers taskUsers = taskUsersRepository.findTaskUsersByTaskIdAndIsResponsible(task.getTaskId(), true);
                List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(taskUsers.getUserId());
                notificationEntities.add(Notification.builder()
                        .taskId(task.getTaskId())
                        .userId(taskUsers.getUserId())
                        .title("Deadline in high status")
                        .description("Deadline in")
                        .status(1)
                        .timeRecive(LocalDateTime.now())
                        .createDate(LocalDateTime.now())
                        .build());
                if (DataUtils.listNotNullOrEmpty(deviceUsers)) {

                    deviceUsers.forEach(i -> {
                        userIds.add(i.getUserId());
                    });
                }
            }
            if (DataUtils.listNotNullOrEmpty(userIds)) {

                MessageDto messageDtoList = MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Deadline in high status").body("Deadline in").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }
        }
        if (DataUtils.notNull(listModerateTask)) {
            List<Long> userIds = new ArrayList<>();
            for (Task task : listModerateTask) {
                TaskUsers taskUsers = taskUsersRepository.findTaskUsersByTaskIdAndIsResponsible(task.getTaskId(), true);
                List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(taskUsers.getUserId());
                notificationEntities.add(Notification.builder()
                        .taskId(task.getTaskId())
                        .userId(taskUsers.getUserId())
                        .title("Deadline in moderate status")
                        .description("Deadline in")
                        .status(1)
                        .timeRecive(LocalDateTime.now())
                        .createDate(LocalDateTime.now())
                        .build());
                if (DataUtils.listNotNullOrEmpty(deviceUsers)) {

                    deviceUsers.forEach(i -> {
                        userIds.add(i.getUserId());
                    });
                }
            }
            if (DataUtils.listNotNullOrEmpty(userIds)) {

                MessageDto messageDtoList = MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Deadline in moderate status").body("Deadline in").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }
        }
        if(DataUtils.listNotNullOrEmpty(notificationEntities)){
            notificationRepository.saveAll(notificationEntities);
        }
    }
}
