package com.wfms.controller1;

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
    public ResponseEntity<Object> createIssue(@RequestBody IssueDTO issue){
        try {
            return  ResponseEntity.ok().body(issueService.createIssue(issue));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-issue-by-project")
    public ResponseEntity<Object> getIssueByProjectId(@RequestParam(name = "projectId") Long projectId){
        try {
            List<Issue> issueList = issueService.getIssueByProjectId(projectId);
            return  ResponseEntity.ok().body(issueList);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/search-issue")
    public ResponseEntity<Object> searchIssuePaging(@RequestBody ObjectPaging objectPaging){
        try {
            ObjectPaging issueList = issueService.searchIssue(objectPaging);
            return  ResponseEntity.ok().body(issueList);
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
            List<Issue> issue = issueService.getListTask(projectId,null);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-list-issue-in-sprint")
    public ResponseEntity<Object> getListIssueInSprint(@RequestParam(name = "sprintId") Long sprintId){
        try {
            List<Issue> issue = issueService.getListTask(1L,sprintId);
            return  ResponseEntity.ok().body(issue);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-task")
    public ResponseEntity<Object> updateTask(@RequestBody IssueDTO issue){
        try {
            Issue issueUpdate = issueService.updateTask(issue);
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
