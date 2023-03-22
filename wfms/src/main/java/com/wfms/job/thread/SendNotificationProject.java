package com.wfms.job.thread;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.entity.ProjectUsers;
import com.wfms.entity.Projects;
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

import java.util.ArrayList;
import java.util.List;

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
    private FireBaseService fireBaseService;
    private List<Projects> listProjectOneMonth;
    private List<Projects> listProjectTwoWeek;
    private List<Projects> listProjectOneWeek;

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
        if (DataUtils.notNull(listProjectOneMonth)){
            List<MessageDto> messageDtoList = new ArrayList<>();
            for (Projects projects: this.listProjectOneMonth) {
                List<ProjectUsers> projectUsersList =  projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(),1L);
                for (ProjectUsers projectUser: projectUsersList) {
                    messageDtoList.add(MessageDto.builder().to(projectUser.getUserId().toString())
                                            .notification(NotificationDto.builder().title("Dead line in one month").body("Deadline in").build()).build());
                }
            }
            fireBaseService.sendManyNotification(messageDtoList);
        }
        if (DataUtils.notNull(listProjectTwoWeek)){
            List<MessageDto> messageDtoList = new ArrayList<>();
            for (Projects projects: this.listProjectTwoWeek) {
                List<ProjectUsers> projectUsersList =  projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(),1L);
                for (ProjectUsers projectUser: projectUsersList) {
                        messageDtoList.add(MessageDto.builder().to(projectUser.getUserId().toString())
                                .notification(NotificationDto.builder().title("Dead line in two week").body("Deadline in").build()).build());
                }
            }
            fireBaseService.sendManyNotification(messageDtoList);
        }
        if (DataUtils.notNull(listProjectOneWeek)){
            List<MessageDto> messageDtoList = new ArrayList<>();
            for (Projects projects: this.listProjectOneWeek) {
                List<ProjectUsers> projectUsersList =  projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(),1L);
                for (ProjectUsers projectUser: projectUsersList) {
                    messageDtoList.add(MessageDto.builder().to(projectUser.getUserId().toString())
                            .notification(NotificationDto.builder().title("Dead line in one week").body("Deadline in").build()).build());
                }
            }
            fireBaseService.sendManyNotification(messageDtoList);
        }
    }

}
