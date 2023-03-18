package com.wfms.job;

import com.wfms.entity.Issue;
import com.wfms.job.thread.SendNotificationIssue;
import com.wfms.job.thread.UpdateIssue;
import com.wfms.repository.IssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class IssueJob {
    @Autowired
    private IssueRepository issueRepository;
    //@Scheduled(cron = "8 * * * * *")
    public void checkDeadlineProjectAndUpdatePriority(){
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>> Start job check deadline issue <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<=");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarEndDateOneMonth =  Calendar.getInstance();
        Date startDate = new Date();
        calendarEndDateOneMonth.setTime(startDate);
        System.out.println("Ngày ban đầu : " + dateFormat.format(startDate));
        calendarEndDateOneMonth.add(Calendar.DATE, 7);
        System.out.println("Deadline 7 ngày: " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Issue> issueDeadlineSevenDay = issueRepository.getIssueByDeadline(calendarEndDateOneMonth.getTime());
        calendarEndDateOneMonth.add(Calendar.DATE,-2);
        System.out.println("Deadline 5 ngày : " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Issue> issueDeadlineFiveDay =  issueRepository.getIssueByDeadline(calendarEndDateOneMonth.getTime());
        calendarEndDateOneMonth.add(Calendar.DATE,-2);
        System.out.println("Deadline 3 ngày : " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Issue> issueDeadlineThreeDay=  issueRepository.getIssueByDeadline(calendarEndDateOneMonth.getTime());
        calendarEndDateOneMonth.add(Calendar.DATE,-2);
        System.out.println("Deadline 1 ngày : " + dateFormat.format(calendarEndDateOneMonth.getTime()));
        List<Issue> issueDeadlineOneDay =  issueRepository.getIssueByDeadline(calendarEndDateOneMonth.getTime());

        UpdateIssue updateIssue = UpdateIssue.builder()
                    .listIssueOneDay(issueDeadlineOneDay)
                    .listIssueThreeDay(issueDeadlineThreeDay)
                    .listIssueFiveDay(issueDeadlineFiveDay)
                    .listIssueSevenDay(issueDeadlineSevenDay).build();
        Thread updateIssueThread = new Thread(updateIssue);
        updateIssueThread.start();
        SendNotificationIssue sendNotificationIssue = SendNotificationIssue.builder()
                                                        .listIssueOneWeek(issueDeadlineSevenDay)
                                                        .listProject1Day(issueDeadlineOneDay)
                                                        .listProject3Day(issueDeadlineThreeDay)
                                                        .listProject5Day(issueDeadlineFiveDay).build();
        Thread sendNotificationThread = new Thread(sendNotificationIssue);
        updateIssueThread.start();
        sendNotificationThread.start();
    }
}
