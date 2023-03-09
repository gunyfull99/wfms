package com.wfms.controller;

import com.wfms.entity.IssueTypes;
import com.wfms.service.IssueTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/issue-type")
public class IssueTypeController {
    @Autowired
    private IssueTypeService issueTypeService;

    @PostMapping("/create-issue-type")
    public ResponseEntity<IssueTypes> createIssueTypes(@RequestBody IssueTypes issueTypes){
        issueTypeService.createIssueType(issueTypes);
        return  ResponseEntity.ok().body(issueTypes);
    }

    @GetMapping("/list-issue-type")
    public ResponseEntity<List<IssueTypes>> createIssueTypes(){
        return  ResponseEntity.ok().body(issueTypeService.listIssueType());
    }
}
