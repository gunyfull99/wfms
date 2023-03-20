package com.wfms.job;

import com.wfms.entity.Projects;
import com.wfms.job.thread.SendNotificationProject;
import com.wfms.job.thread.UpdateProject;
import com.wfms.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ProjectJob {
    @Autowired
    private ProjectRepository projectRepository;
    //<giây> <phút> <giờ> <ngày> <tháng> <ngày trong tuần>
    @Scheduled(cron = "8 * * * * *")
    public void checkDeadlineProjectAndUpdatePriority(){
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Start job check deadline project <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarEndDateOneMonth =  Calendar.getInstance();
        Date startDate = new Date();
        calendarEndDateOneMonth.setTime(startDate);
        System.out.println("Ngày ban đầu : " + dateFormat.format(startDate));
        calendarEndDateOneMonth.add(Calendar.MONTH, 1);
        System.out.println("Deadline 1 tháng: " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Projects> projectsDeadlineOneMonth = projectRepository.getProjectByDeadline(calendarEndDateOneMonth.getTime());
        calendarEndDateOneMonth.add(Calendar.DATE,-14);
        System.out.println("Deadline 2 tuần : " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Projects> projectsDeadlineTwoWeek =  projectRepository.getProjectByDeadline(calendarEndDateOneMonth.getTime());;
        calendarEndDateOneMonth.add(Calendar.DATE,-6);
        System.out.println("Deadline 1 tuần : " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Projects> projectsDeadlineOneWeek =  projectRepository.getProjectByDeadline(calendarEndDateOneMonth.getTime());;
        UpdateProject updateProject = UpdateProject.builder().listProjectOneMonth(projectsDeadlineOneMonth)
                                                                .listProjectOneWeek(projectsDeadlineOneWeek)
                                                                .listProjectTwoWeek(projectsDeadlineTwoWeek).build();
        SendNotificationProject sendNotificationProject = SendNotificationProject.builder().listProjectOneMonth(projectsDeadlineOneMonth)
                                                                                            .listProjectOneWeek(projectsDeadlineOneWeek)
                                                                                            .listProjectTwoWeek(projectsDeadlineTwoWeek).build();
        Thread updatePro = new Thread(updateProject);
        Thread sendNotificationThread = new Thread(sendNotificationProject);
        updatePro.start();
        sendNotificationThread.start();
    }
}
