package com.wfms.job;

import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.entity.Event;
import com.wfms.entity.Notification;
import com.wfms.repository.NotificationRepository;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.repository.EventRepository;
import com.wfms.service.FireBaseService;
import com.wfms.utils.Constants;
import com.wfms.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


 import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import  com.wfms.entity.ProjectUsers;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SchedulesJob {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    //<giây> <phút> <giờ> <ngày> <tháng> <ngày trong tuần>
   // @Scheduled(cron = "* */50 * * * *")
    public void sendScheduleMeeting(){
        try{
            log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Send notifilecation event <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
            List<Event> schedules = eventRepository.findEventWithMeetingInOneHour(LocalDateTime.now());
            List<Notification> notificationEntities = new ArrayList<>();
            boolean check=false;
            for (Event schedule: schedules) {
                List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(schedule.getProjectId(), 1);
                NotificationDto notificationDto = NotificationDto.builder().title(schedule.getMeetingTitle()).body(schedule.getMeetingDescription() + " sẽ diễn ra sau 1 tiếng nữa hãy chú ý thời gian").build();
                if(DataUtils.listNotNullOrEmpty(projectUsersList)){
                    List<Long> userId = projectUsersList.stream().distinct().map(ProjectUsers::getUserId).collect(Collectors.toList());
                    if(DataUtils.listNotNullOrEmpty(userId)){
                    projectUsersList.forEach(o->{
                        notificationEntities.add(Notification.builder()
                                .projectId(o.getProjectId())
                                .userId(o.getUserId())
                                .title("You have a meeting in 1 hour")
                                .description("Meeting "+schedule.getMeetingTitle()+" will take place in 1 hour")
                                .status(1)
                                .timeRecive(LocalDateTime.now())
                                .createDate(LocalDateTime.now())
                                .build());
                    });
                    MessageDto messageDto =  MessageDto.builder().notification(notificationDto).userId(userId).build();
                        fireBaseService.sendManyNotification(messageDto);
                        check=true;
                    }
                }
            }
            if(check){
                notificationRepository.saveAll(notificationEntities);
            }
            log.info("=>>>>>>>>>>>>>>>>>>>>>>>> End Send notifilecation event<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
