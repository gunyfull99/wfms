package com.wfms.controller;

import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.SprintDTO;
import com.wfms.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/sprint")
public class SprintController {

    @Autowired
    private SprintService sprintService;


    @PostMapping("/create-sprint")
    public ResponseEntity<Object> createSprint(@RequestBody SprintDTO sprintDTO){
        try {
            return  ResponseEntity.ok().body(   sprintService.createSprint(sprintDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-sprint")
    public ResponseEntity<Object> updateSprint(@RequestBody SprintDTO sprintDTO){
        try {
            return  ResponseEntity.ok().body(sprintService.updateSprint(sprintDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetailSprint(@PathVariable (value = "id") Long sprintId){
        try {
            return  ResponseEntity.ok().body( sprintService.getDetailSprint(sprintId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
