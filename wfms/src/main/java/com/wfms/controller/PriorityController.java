package com.wfms.controller;

import com.wfms.Dto.PriorityDTO;
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
    public ResponseEntity<Object> createPriority(@RequestBody PriorityDTO priority){
        try {
            return  ResponseEntity.ok().body(priorityService.createPriority(priority));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/list")
    public ResponseEntity<Object> getAllPriority(){
        try {
            return new ResponseEntity<>(priorityService.findAll(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
