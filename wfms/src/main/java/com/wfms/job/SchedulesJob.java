package com.wfms.job;

import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.entity.Schedules;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.repository.SchedulesRepository;
import com.wfms.service.FireBaseService;
import com.wfms.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import  com.wfms.entity.ProjectUsers;
@Slf4j
public class SchedulesJob {
    @Autowired
    private SchedulesRepository schedulesRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    //<giây> <phút> <giờ> <ngày> <tháng> <ngày trong tuần>
    // @Scheduled(cron = "8 * * * * *")
    public void updateSchedulesWithEndDate(){
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Start Scan Schedules and update status schedules <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        LocalDateTime localDateTime = LocalDateTime.now();
        schedulesRepository.updateStatusSchedules(localDateTime);
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> End Scan Schedules and update status schedules <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
    }

     //@Scheduled(cron = "8 * * * * *")
    public void sendScheduleMeeting(){
        try{
            log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Send notifilecation schedule <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
            LocalDateTime localDateTime = LocalDateTime.now();
            System.out.println("Time scan schedule is "+ localDateTime);
            List<Schedules> schedules = schedulesRepository.findScheduleWithMeetingInOneHour(localDateTime);
            for (Schedules schedule: schedules) {
                List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(schedule.getProjectId(), Constants.ACTIVE);
                NotificationDto notificationDto = NotificationDto.builder().title(schedule.getMeetingTitle()).body(schedule.getMeetingDescription() + " sẽ diễn ra sau 1 tiếng nữa hãy chú ý thời gian").build();
                List<Long> userId = projectUsersList.stream().map(i->i.getUserId()).collect(Collectors.toList());
                MessageDto messageDto =  MessageDto.builder().notification(notificationDto).userId(userId).build();
                fireBaseService.sendManyNotification(messageDto);
            }
            log.info("=>>>>>>>>>>>>>>>>>>>>>>>> End Send notifilecation schedule<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
