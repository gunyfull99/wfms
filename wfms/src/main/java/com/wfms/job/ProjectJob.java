package com.wfms.job;

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
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ProjectJob {
    @Autowired
    private ProjectRepository projectRepository;
    //<giây> <phút> <giờ> <ngày> <tháng> <ngày trong tuần>
    @Scheduled(cron = "* */10 * * * *")
    public void checkDeadlineProjectAndUpdatePriority(){
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Start job check deadline project <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        List<Projects>exProject=new ArrayList<>();
        List<Projects>highProject=new ArrayList<>();
        List<Projects>moderProject=new ArrayList<>();
        List<Projects> projects = projectRepository.getProjectActive();
        if(DataUtils.listNotNullOrEmpty(projects)) {
            projects.forEach(o -> {
                Assert.notNull(o.getDeadLine(), "Deadline dự án không được để trống");
                Assert.notNull(o.getStartDate(), "StartDate dự án không được để trống");
                Assert.notNull(o.getPriorityId(), "Mức độ ưu tiên dự án không được để trống");
                if (Constants.HIGH.equals(o.getPriorityId())) {
                    Date d = DataUtils.getPeriodDate(o.getStartDate(), o.getDeadLine(), Constants.PERIOD_1);
                    if (new Date().after(d)) {
                        exProject.add(o);
                    }
                } else if (Constants.MODERATE.equals(o.getPriorityId())) {
                    Date d = DataUtils.getPeriodDate(o.getStartDate(), o.getDeadLine(), Constants.PERIOD_2);
                    if (new Date().after(d)) {
                        highProject.add(o);
                    }
                } else if (Constants.LOW.equals(o.getPriorityId())) {
                    Date d = DataUtils.getPeriodDate(o.getStartDate(), o.getDeadLine(), Constants.PERIOD_3);
                    if (new Date().after(d)) {
                        moderProject.add(o);
                    }
                }
            });
        }
        UpdateProject updateProject = UpdateProject.builder().listExtremeProject(exProject)
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
