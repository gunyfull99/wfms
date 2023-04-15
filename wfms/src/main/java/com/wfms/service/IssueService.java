package com.wfms.service;

import com.wfms.Dto.ChartIssue;
import com.wfms.Dto.ChartResponseDto;
import com.wfms.Dto.IssueDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.Issue;
import com.wfms.entity.IssueUsers;

import java.util.List;

public interface IssueService {
    List<Issue> getIssueByUserId(Long userId);
    List<Issue> getIssueByUserIdAndProjectId(Long userId,Long projectId);
    Issue createIssue(String token,IssueDTO issue);
    List<IssueDTO> getIssueByProjectId(Long projectId);
    IssueDTO getDetailIssueById(Long issueId);
    Issue updateTask(String token ,IssueDTO issue);
    List<IssueUsers> updateAssignessTask(List<IssueUsers> issueUsers);
    List<IssueDTO> getListTask(Long projectId,Long sprintId);
    ObjectPaging searchIssue( ObjectPaging objectPaging);
    List<List<ChartIssue>>  chartIssue(Long projectId,Boolean inBackLog,Integer status);
    String requestToIssue(String token,Long issueId);
    List<ChartResponseDto> getstatisticTask (Long projectId);


}
