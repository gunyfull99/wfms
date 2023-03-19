package com.wfms.controller;

import com.wfms.Dto.CommentIssueDTO;
import com.wfms.service.CommentIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentIssueService commentIssueService;

    @PostMapping("/create-comment")
    public ResponseEntity<Object> createComment(@RequestBody CommentIssueDTO commentIssueDTO){
        try {
            return  ResponseEntity.ok().body(  commentIssueService.createComment(commentIssueDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
