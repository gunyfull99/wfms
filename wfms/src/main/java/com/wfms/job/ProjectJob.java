package com.wfms.job;

import com.wfms.config.Const;
import com.wfms.entity.Projects;
import com.wfms.job.thread.SendNotificationProject;
import com.wfms.job.thread.UpdateProject;
import com.wfms.repository.ProjectRepository;
import com.wfms.utils.Constants;
import com.wfms.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
 import java.time.LocalDateTime; 
import java.util.List;

@Slf4j
@Service
public class ProjectJob {
    @Autowired
    private ProjectRepository projectRepository;
    //<giây> <phút> <giờ> <ngày> <tháng> <ngày trong tuần>
    //@Scheduled(cron = "* */10 * * * *")
    public void checkDeadlineProjectAndUpdatePriority(){
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Start job check deadline project <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        List<Projects>exProject=new ArrayList<>();
        List<Projects>highProject=new ArrayList<>();
        List<Projects>moderProject=new ArrayList<>();
        List<Projects> projects = projectRepository.getProjectActive();
        if(DataUtils.listNotNullOrEmpty(projects)) {
            projects.forEach(o -> {
                Assert.notNull(o.getDeadLine(), Const.responseError.deadline_null);
                Assert.notNull(o.getStartDate(), "StartDate project must not be null");
                Assert.notNull(o.getPriorityId(), Const.responseError.priorityId_null);
                if (Constants.HIGH.equals(o.getPriorityId())) {
                    LocalDateTime d = DataUtils.getPeriodDate(o.getStartDate(), o.getDeadLine(), Constants.PERIOD_1);
                    if (LocalDateTime.now().isAfter(d)) {
                        exProject.add(o);
                    }
                } else if (Constants.MODERATE.equals(o.getPriorityId())) {
                    LocalDateTime d = DataUtils.getPeriodDate(o.getStartDate(), o.getDeadLine(), Constants.PERIOD_2);
                    if (LocalDateTime.now().isAfter(d)) {
                        highProject.add(o);
                    }
                } else if (Constants.LOW.equals(o.getPriorityId())) {
                    LocalDateTime d = DataUtils.getPeriodDate(o.getStartDate(), o.getDeadLine(), Constants.PERIOD_3);
                    if (LocalDateTime.now().isAfter(d)) {
                        moderProject.add(o);
                    }
                }
            });
        }
        UpdateProject updateProject = UpdateProject.builder().listExtremeProject(exProject)
                .projectRepository(projectRepository)
                                                                .listHighProject(highProject)
                                                                .listModerateProject(moderProject).build();
        SendNotificationProject sendNotificationProject = SendNotificationProject.builder().listExtremeProject(exProject)
                                                                                            .listHighProject(highProject)
                                                                                            .listModerateProject(moderProject).build();
        Thread updatePro = new Thread(updateProject);
        Thread sendNotificationThread = new Thread(sendNotificationProject);
        updatePro.start();
        sendNotificationThread.start();
    }
}
