package com.wfms.controller;

import com.wfms.entity.TaskTypes;
import com.wfms.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/task-type")
public class TaskTypeController {
    @Autowired
    private TaskTypeService taskTypeService;

    @PostMapping("/create-task-type")
    public ResponseEntity<Object> createTaskTypes(@RequestBody TaskTypes taskTypes){
        try {
            return  ResponseEntity.ok().body(taskTypeService.createTaskType(taskTypes));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list-task-type")
    public ResponseEntity<Object> listTaskTypes(){
        try {
            return  ResponseEntity.ok().body(taskTypeService.listTaskType());
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
