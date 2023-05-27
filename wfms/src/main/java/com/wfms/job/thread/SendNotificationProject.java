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

    private ProjectUsersRepository projectUsersRepository;
    private DevicesUsersRepository devicesUsersRepository;
    private NotificationRepository notificationRepository;
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
                List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(), 1);
                for (ProjectUsers projectUser : projectUsersList) {
                    notificationEntities.add(Notification.builder()
                            .projectId(projectUser.getProjectId())
                            .userId(projectUser.getUserId())
                            .title("Priority project changed to extreme status")
                            .description("Project "+projects.getProjectName()+" 's priority was changed to extreme because it was 85% of the time")
                            .status(1)
                            .timeRecive(LocalDateTime.now())
                            .createDate(LocalDateTime.now())
                            .build());
                    List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(projectUser.getUserId());
                    if (DataUtils.listNotNullOrEmpty(deviceUsers)) {
                        userIds.add(projectUser.getUserId());
                    }

                }
            }
            if (DataUtils.listNotNullOrEmpty(userIds)) {

                MessageDto messageDtoList = MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Priority project changed to extreme status")
                                .body("Priority of project was changed to extreme because it was 85% of the time").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }
        }
        if (DataUtils.notNull(listHighProject)){
            List<Long> userIds=new ArrayList<>();
            for (Projects projects: this.listHighProject) {
                List<ProjectUsers> projectUsersList =  projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(),1);
                for (ProjectUsers projectUser: projectUsersList) {
                    notificationEntities.add(Notification.builder()
                            .projectId(projectUser.getProjectId())
                            .userId(projectUser.getUserId())
                            .title("Priority project changed to high status")
                            .description("Project "+projects.getProjectName()+" 's priority was changed to high because it was 70% of the time")
                            .status(1)
                            .timeRecive(LocalDateTime.now())
                            .createDate(LocalDateTime.now())
                            .build());
                    List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(projectUser.getUserId());
                    if (DataUtils.listNotNullOrEmpty(deviceUsers)) {
                        userIds.add(projectUser.getUserId());
                    }
                }
            }
            if(DataUtils.listNotNullOrEmpty(userIds)){
                MessageDto messageDtoList =   MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Priority project changed to high status").body("Priority of project was changed to high because it was 70% of the time").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }

        }
        if (DataUtils.notNull(listModerateProject)) {
            List<Long> userIds = new ArrayList<>();
            for (Projects projects : this.listModerateProject) {
                List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(), 1);
                for (ProjectUsers projectUser : projectUsersList) {
                    notificationEntities.add(Notification.builder()
                            .projectId(projectUser.getProjectId())
                            .userId(projectUser.getUserId())
                            .title("Priority project changed to moderate status")
                            .description("Project "+projects.getProjectName()+" 's priority was changed to moderate because it was 60% of the time")
                            .status(1)
                            .timeRecive(LocalDateTime.now())
                            .createDate(LocalDateTime.now())
                            .build());
                    List<DeviceUsers> deviceUsers = devicesUsersRepository.findDeviceByUserId(projectUser.getUserId());
                    if (DataUtils.listNotNullOrEmpty(deviceUsers)) {
                        userIds.add(projectUser.getUserId());
                    }
                }
            }
            if (DataUtils.listNotNullOrEmpty(userIds)) {
                MessageDto messageDtoList = MessageDto.builder().userId(userIds)
                        .notification(NotificationDto.builder().title("Priority project changed to moderate status").body("Priority of project was changed to moderate because it was 60% of the time").build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
            }
        }
        if(DataUtils.listNotNullOrEmpty(notificationEntities)){
            notificationRepository.saveAll(notificationEntities);
        }
    }

}
