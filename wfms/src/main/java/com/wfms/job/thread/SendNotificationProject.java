package com.wfms.job.thread;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.entity.DeviceUsers;
import com.wfms.entity.Notification;
import com.wfms.entity.ProjectUsers;
import com.wfms.entity.Projects;
import com.wfms.repository.DevicesUsersRepository;
import com.wfms.repository.NotificationRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.service.FireBaseService;
import com.wfms.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@Slf4j
public class SendNotificationProject extends Thread {
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    @Autowired
    private DevicesUsersRepository devicesUsersRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FireBaseService fireBaseService;
    private List<Projects> listExtremeProject;
    private List<Projects> listHighProject;
    private List<Projects> listModerateProject;

    @Override
    public void run(){
        try {
            this.sendNotification();
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void sendNotification() throws FirebaseMessagingException {
        List<Notification> notificationEntities = new ArrayList<>();
        if (DataUtils.notNull(listExtremeProject)) {
            List<Long> userIds = new ArrayList<>();
            for (Projects projects : this.listExtremeProject) {
                List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(), 3);
                for (ProjectUsers projectUser : projectUsersList) {
                    notificationEntities.add(Notification.builder()
                            .projectId(projectUser.getProjectId())
                            .userId(projectUser.getUserId())
                            .title("Dead line in extreme status")
                            .description("Deadline in")
                            .status(1)
                            .timeRecive(LocalDateTime.now())
                            .createDate(LocalDateTime.now())
                            .build());
                    List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(projectUser.getUserId());
                    if (DataUtils.listNotNullOrEmpty(deviceUsers)) {
                        deviceUsers.forEach(i -> {
                            userIds.add(i.getUserId());
                        });
                    }

                }
            }
            if (DataUtils.listNotNullOrEmpty(userIds)) {

                MessageDto messageDtoList = MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Dead line in extreme status").body("Deadline in").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }
        }
        if (DataUtils.notNull(listHighProject)){
            List<Long> userIds=new ArrayList<>();
            for (Projects projects: this.listHighProject) {
                List<ProjectUsers> projectUsersList =  projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(),3);
                for (ProjectUsers projectUser: projectUsersList) {
                    notificationEntities.add(Notification.builder()
                            .projectId(projectUser.getProjectId())
                            .userId(projectUser.getUserId())
                            .title("Dead line in high status")
                            .description("Deadline in")
                            .status(1)
                            .timeRecive(LocalDateTime.now())
                            .createDate(LocalDateTime.now())
                            .build());
                    List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(projectUser.getUserId());
                    if(DataUtils.listNotNullOrEmpty(deviceUsers)){
                        deviceUsers.forEach(i-> {
                            userIds.add(i.getUserId());
                        });
                    }
                }
            }
            if(DataUtils.listNotNullOrEmpty(userIds)){
                MessageDto messageDtoList =   MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Dead line in high status").body("Deadline in").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }

        }
        if (DataUtils.notNull(listModerateProject)) {
            List<Long> userIds = new ArrayList<>();
            for (Projects projects : this.listModerateProject) {
                List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(), 3);
                for (ProjectUsers projectUser : projectUsersList) {
                    notificationEntities.add(Notification.builder()
                            .projectId(projectUser.getProjectId())
                            .userId(projectUser.getUserId())
                            .title("Dead line in moderate status")
                            .description("Deadline in")
                            .status(1)
                            .timeRecive(LocalDateTime.now())
                            .createDate(LocalDateTime.now())
                            .build());
                    List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(projectUser.getUserId());
                    if (DataUtils.listNotNullOrEmpty(deviceUsers)) {
                        deviceUsers.forEach(i -> {
                            userIds.add(i.getUserId());
                        });
                    }
                }
            }
            if (DataUtils.listNotNullOrEmpty(userIds)) {
                MessageDto messageDtoList = MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Dead line in moderate status").body("Deadline in").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }
        }
        if(DataUtils.listNotNullOrEmpty(notificationEntities)){
            notificationRepository.saveAll(notificationEntities);
        }
    }

}
