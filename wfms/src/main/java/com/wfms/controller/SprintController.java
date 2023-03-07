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
    public ResponseEntity<?> createSprint(@RequestBody SprintDTO sprintDTO){
        try{
            sprintService.createSprint(sprintDTO);
            return new ResponseEntity<>(sprintDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-sprint")
    public ResponseEntity<?> updateSprint(@RequestBody SprintDTO sprintDTO){
        try{
            sprintService.updateSprint(sprintDTO);
            return new ResponseEntity<>(sprintDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailSprint(@PathVariable (value = "id") Long sprintId){
        try{
            return new ResponseEntity<>( sprintService.getDetailSprint(sprintId), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
