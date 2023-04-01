package com.wfms.service;

import com.wfms.Dto.IssueDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.Issue;
import com.wfms.entity.IssueUsers;

import java.util.List;

public interface IssueService {
    List<Issue> getIssueByUserId(Long userId);
    List<Issue> getIssueByUserIdAndProjectId(Long userId,Long projectId);
    Issue createIssue(IssueDTO issue);
    List<Issue> getIssueByProjectId(Long projectId);
    IssueDTO getDetailIssueById(Long issueId);
    Issue updateTask(IssueDTO issue);
    List<IssueUsers> updateAssignessTask(List<IssueUsers> issueUsers);
    List<Issue> getListTask(Long projectId,Long sprintId);
    ObjectPaging searchIssue( ObjectPaging objectPaging);


}
