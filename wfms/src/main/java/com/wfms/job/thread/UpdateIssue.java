package com.wfms.job.thread;

import com.wfms.entity.Issue;
import com.wfms.entity.Projects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateIssue extends Thread{
    private List<Issue> listIssueOneDay;
    private List<Issue> listIssueThreeDay;
    private List<Issue> listIssueFiveDay;
    private List<Issue> listIssueSevenDay;

    @Override
    public void run(){
        this.updateIssuePriority();
    }
    public void updateIssuePriority(){

    }
}
