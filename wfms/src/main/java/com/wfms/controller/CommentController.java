package com.wfms.controller;

import com.wfms.service.CommentIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentIssueService commentIssueService;

    @PostMapping(value = "/create-comment",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> createComment(@RequestPart String commentIssueDTO, @RequestPart List<MultipartFile> images ){
        try {
            return  ResponseEntity.ok().body(  commentIssueService.createComment(commentIssueDTO,images));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/getListCommentByIssue")
    public ResponseEntity<Object> getListCommentByIssue(@RequestParam(name = "issueId") Long issueId){
        try {
            return  ResponseEntity.ok().body(  commentIssueService.getCommentByIssue(issueId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/getUrlImage")
    public ResponseEntity<Object> getUrlImage(@RequestParam(name = "nameFile") String image){
        try {
            return  ResponseEntity.ok().body(  commentIssueService.getUrlFile(image));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/getFile")
    public ResponseEntity<Object> getFile(@RequestParam(name = "nameFile") String image){
        try {
            return  ResponseEntity.ok().body(  commentIssueService.getFile(image));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
