package com.wfms.controller;

import com.wfms.Dto.ProjectDTO;
import com.wfms.entity.Priority;
import com.wfms.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/priority")
public class PriorityController {
    @Autowired
    private PriorityService priorityService;

    @PostMapping("/create-priority")
    public ResponseEntity<Priority> createProject(@RequestBody Priority priority){
            priorityService.createPriority(priority);
            return  ResponseEntity.ok().body(priority);

    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllProject(){
        try{
            return new ResponseEntity<>(priorityService.findAll(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
