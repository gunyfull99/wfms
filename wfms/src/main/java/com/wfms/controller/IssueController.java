package com.wfms.controller;

import com.wfms.Dto.IssueDTO;
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
    public ResponseEntity<Object> createIssue(@RequestBody IssueDTO issue){
        try {
            return  ResponseEntity.ok().body(issueService.createIssue(issue));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-issue-by-project/{projectId}")
    public ResponseEntity<Object> getIssueByProjectId(@PathVariable Long projectId){
        try {
            List<Issue> issueList = issueService.getIssueByProjectId(projectId);
            return  ResponseEntity.ok().body(issueList);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-issue-by-id/{issueId}")
    public ResponseEntity<Object> getDetailIssueById(@PathVariable Long issueId){
        try {
            Issue issue = issueService.getDetailIssueById(issueId);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-list-issue-in-backlog")
    public ResponseEntity<Object> getListIssueInBackLog(){
        try {
            List<Issue> issue = issueService.getListTask(null);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-list-issue-in-sprint")
    public ResponseEntity<Object> getListIssueInSprint(@RequestParam(name = "sprintId") Long sprintId){
        try {
            List<Issue> issue = issueService.getListTask(sprintId);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-task")
    public ResponseEntity<Object> updateTask(@RequestBody IssueDTO issue){
        try {
            Issue issueUpdate = issueService.updateTaskDoneOrNotDone(issue);
            return  ResponseEntity.ok().body(issueUpdate);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-assigness")
    public ResponseEntity<Object> updateAssigness(@RequestBody IssueUsers issueUsers){
        try {
            IssueUsers issueUpdate = issueService.updateAssignessTask(issueUsers);
            return  ResponseEntity.ok().body(issueUpdate);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
