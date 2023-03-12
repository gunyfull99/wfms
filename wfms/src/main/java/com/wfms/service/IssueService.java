package com.wfms.service;

import com.wfms.Dto.IssueDTO;
import com.wfms.entity.Issue;
import com.wfms.entity.IssueUsers;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IssueService {
    List<Issue> getIssueByUserId(Long userId);
    List<Issue> getIssueByUserIdAndProjectId(Long userId,Long projectId);

    Issue createIssue(IssueDTO issue);

    List<Issue> getIssueByProjectId(Long projectId);
    Issue getDetailIssueById(Long issueId);

    Issue updateTaskDoneOrNotDone(IssueDTO issue);

    IssueUsers updateAssignessTask(IssueUsers issueUsers);
}
