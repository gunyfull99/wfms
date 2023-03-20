package com.wfms.job.thread;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.entity.Issue;
import com.wfms.entity.IssueUsers;
import com.wfms.entity.Projects;
import com.wfms.repository.IssueUsersRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.service.FireBaseService;
import com.wfms.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class SendNotificationIssue extends Thread{
    @Autowired
    private IssueUsersRepository issueUsersRepository;
    @Autowired
    private FireBaseService fireBaseService;
    private List<Issue> listIssueOneWeek;
    private List<Issue> listProject3Day;
    private List<Issue> listProject1Day;
    private List<Issue> listProject5Day;

    @Override
    public void run(){
        try {
            this.sendNotification();
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendNotification() throws FirebaseMessagingException {
        if (DataUtils.notNull(listIssueOneWeek)){
            List<MessageDto> messageDtoList = new ArrayList<>();
            for (Issue issue: this.listIssueOneWeek) {
                IssueUsers issueUsers =  issueUsersRepository.findIssueUsersByIssueIdAndIsResponsible(issue.getIssueId(),true);
                messageDtoList.add(MessageDto.builder().to(issueUsers.getUserId().toString())
                        .notification(NotificationDto.builder().title("Deadline in one week").body("Deadline in").build()).build());
            }
            fireBaseService.sendManyNotification(messageDtoList);
        }
        if (DataUtils.notNull(listProject3Day)){
            List<MessageDto> messageDtoList = new ArrayList<>();
            for (Issue issue: this.listProject3Day) {
                IssueUsers issueUsers =  issueUsersRepository.findIssueUsersByIssueIdAndIsResponsible(issue.getIssueId(),true);
                messageDtoList.add(MessageDto.builder().to(issueUsers.getUserId().toString())
                        .notification(NotificationDto.builder().title("Deadline in 3 day").body("Deadline in").build()).build());
            }
            fireBaseService.sendManyNotification(messageDtoList);
        }
        if (DataUtils.notNull(listProject1Day)){
            List<MessageDto> messageDtoList = new ArrayList<>();
            for (Issue issue: this.listProject1Day) {
                IssueUsers issueUsers =  issueUsersRepository.findIssueUsersByIssueIdAndIsResponsible(issue.getIssueId(),true);
                messageDtoList.add(MessageDto.builder().to(issueUsers.getUserId().toString())
                        .notification(NotificationDto.builder().title("Deadline in one day").body("Deadline in").build()).build());
            }
            fireBaseService.sendManyNotification(messageDtoList);
        }
        if (DataUtils.notNull(listProject5Day)){
            List<MessageDto> messageDtoList = new ArrayList<>();
            for (Issue issue: this.listProject5Day) {
                IssueUsers issueUsers =  issueUsersRepository.findIssueUsersByIssueIdAndIsResponsible(issue.getIssueId(),true);
                messageDtoList.add(MessageDto.builder().to(issueUsers.getUserId().toString())
                        .notification(NotificationDto.builder().title("Deadline in five day").body("Deadline in").build()).build());
            }
            fireBaseService.sendManyNotification(messageDtoList);
        }
    }
}
