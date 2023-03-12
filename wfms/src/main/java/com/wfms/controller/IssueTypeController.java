package com.wfms.controller;

import com.wfms.entity.IssueTypes;
import com.wfms.entity.IssueUsers;
import com.wfms.service.IssueTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> createIssueTypes(@RequestBody IssueTypes issueTypes){
        try {
            return  ResponseEntity.ok().body(issueTypeService.createIssueType(issueTypes));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list-issue-type")
    public ResponseEntity<Object> createIssueTypes(){
        try {
            return  ResponseEntity.ok().body(issueTypeService.listIssueType());
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
