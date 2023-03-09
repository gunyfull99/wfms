package com.wfms.controller;

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
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue){
            issueService.createIssue(issue);
            return  ResponseEntity.ok().body(issue);

    }
    @GetMapping("/get-issue-by-project/{projectId}")
    public ResponseEntity<List<Issue>> getIssueByProjectId(@PathVariable Long projectId){
            List<Issue> issueList = issueService.getIssueByProjectId(projectId);
            return  ResponseEntity.ok().body(issueList);
    }
    @GetMapping("/get-issue-by-id/{issueId}")
    public ResponseEntity<Issue> getDetailIssueById(@PathVariable Long issueId){
        Issue issue = issueService.getDetailIssueById(issueId);
        return  ResponseEntity.ok().body(issue);
    }
    @PostMapping("/update-task")
    public ResponseEntity<?> updateTask(@RequestBody Issue issue){
            Issue issueUpdate = issueService.updateTaskDoneOrNotDone(issue);
            return  ResponseEntity.ok().body(issueUpdate);
    }
    @GetMapping("/update-assigness")
    public ResponseEntity<IssueUsers> updateAssigness(@RequestBody IssueUsers issueUsers){

            IssueUsers issueUpdate = issueService.updateAssignessTask(issueUsers);
            return  ResponseEntity.ok().body(issueUpdate);

    }

}
