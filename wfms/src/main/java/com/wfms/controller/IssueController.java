package com.wfms.controller;

import com.wfms.Dto.ChartIssue;
import com.wfms.Dto.IssueDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.Issue;
import com.wfms.entity.IssueUsers;
import com.wfms.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issue")
public class IssueController {
    @Autowired
    private IssueService issueService;
    @PostMapping("/create-issue")
    public ResponseEntity<Object> createIssue(@RequestHeader("Authorization") String token,@RequestBody IssueDTO issue){
        try {
            return  ResponseEntity.ok().body(issueService.createIssue(token, issue));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-issue-by-project")
    public ResponseEntity<Object> getIssueByProjectId(@RequestParam(name = "projectId") Long projectId){
        try {
            List<IssueDTO> issueList = issueService.getIssueByProjectId(projectId);
            return  ResponseEntity.ok().body(issueList);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/search-issue")
    public ResponseEntity<Object> searchIssuePaging(@RequestBody ObjectPaging objectPaging){
        try {
            ObjectPaging issueList = issueService.searchIssue(objectPaging);
            return  ResponseEntity.ok().body(issueList);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/request-join-issue")
    public ResponseEntity<Object> requestJoinIssue(@RequestHeader("Authorization") String token,@RequestParam(name = "issueId") Long issueId){
        try {
            return  ResponseEntity.ok().body( issueService.requestToIssue(token,issueId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-issue-in-chart-in-sprint")
    public ResponseEntity<Object> listIssueInChartInSprint(@RequestParam(name = "projectId") Long projectId){
        try {
            List<List<ChartIssue>>  issue = issueService.chartIssue(projectId, false,3);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-issue-in-chart-in-sprint-complete")
    public ResponseEntity<Object> listIssueInChartInSprintComplete(@RequestParam(name = "projectId") Long projectId){
        try {
            List<List<ChartIssue>>  issue = issueService.chartIssue(projectId, false,2);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-issue-in-chart-in-backlog")
    public ResponseEntity<Object> listIssueInChartInBackLog(@RequestParam(name = "projectId") Long projectId){
        try {
            List<List<ChartIssue>>  issue = issueService.chartIssue(projectId, true,0);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-issue-by-id")
    public ResponseEntity<Object> getDetailIssueById(@RequestParam(name = "issueId") Long issueId){
        try {
            IssueDTO issue = issueService.getDetailIssueById(issueId);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-list-issue-in-backlog")
    public ResponseEntity<Object> getListIssueInBackLog(@RequestParam(name = "projectId") Long projectId){
        try {
            List<IssueDTO> issue = issueService.getListTask(projectId,null);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-list-issue-in-sprint")
    public ResponseEntity<Object> getListIssueInSprint(@RequestParam(name = "sprintId") Long sprintId){
        try {
            List<IssueDTO> issue = issueService.getListTask(1L,sprintId);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-task")
    public ResponseEntity<Object> updateTask(@RequestHeader("Authorization") String token,@RequestBody IssueDTO issue){
        try {
            Issue issueUpdate = issueService.updateTask(token, issue);
            return  ResponseEntity.ok().body(issueUpdate);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-assigness")
    public ResponseEntity<Object> updateAssigness(@RequestBody List<IssueUsers> issueUsers){
        try {
            List<IssueUsers> issueUpdate = issueService.updateAssignessTask(issueUsers);
            return  ResponseEntity.ok().body(issueUpdate);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
