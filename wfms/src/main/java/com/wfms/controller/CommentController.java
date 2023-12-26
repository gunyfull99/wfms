package com.wfms.controller;

import com.wfms.service.CommentTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentTaskService commentTaskService;


    @PostMapping(value = "/create-comment",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> createComment(@RequestPart String commentTaskDTO,@Nullable @RequestPart List<MultipartFile> images ){
        try {
            return  ResponseEntity.ok().body(  commentTaskService.createComment(commentTaskDTO,images));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/update-comment",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateComment(@RequestPart String commentTaskDTO,@Nullable @RequestPart String listImagesWantDelete,@Nullable @RequestPart List<MultipartFile> images ){
        try {
            return  ResponseEntity.ok().body(  commentTaskService.updateComment(commentTaskDTO,listImagesWantDelete,images));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/getListCommentByTask")
    public ResponseEntity<Object> getListCommentByTask(@RequestParam(name = "taskId") Long taskId){
        try {
            return  ResponseEntity.ok().body(  commentTaskService.getCommentByTask(taskId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/get-detail-comment")
    public ResponseEntity<Object> getDetailComment(@RequestParam(name = "commentTaskId") Long commentTaskId){
        try {
            return  ResponseEntity.ok().body(  commentTaskService.getDetailComment(commentTaskId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/delete-comment")
    public ResponseEntity<Object> deleteComment(@RequestParam(name = "commentTaskId") Long commentTaskId){
        try {
            return  ResponseEntity.ok().body(  commentTaskService.deleteComment(commentTaskId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/getUrlImage")
    public ResponseEntity<Object> getUrlImage(@RequestParam(name = "nameFile") String image){
        try {
            return  ResponseEntity.ok().body(  commentTaskService.getUrlFile(image));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/getFile")
    public ResponseEntity<Object> getFile(@RequestParam(name = "nameFile") String image){
        try {
            return  ResponseEntity.ok().body(  commentTaskService.getFile(image));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
