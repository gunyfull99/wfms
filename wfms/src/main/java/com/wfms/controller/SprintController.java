package com.wfms.controller;

import com.wfms.Dto.SprintDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.Sprint;
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


    @PostMapping(value = "/create-sprint")
    public ResponseEntity<Object> createSprint(@RequestBody SprintDTO sprintDTO){
        try {
            return  ResponseEntity.ok().body(sprintService.createSprint(sprintDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-sprint")
    public ResponseEntity<Object> updateSprint(@RequestBody Sprint sprintDTO){
        try {
            return  ResponseEntity.ok().body(sprintService.updateSprint(sprintDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/complete-sprint")
    public ResponseEntity<Object> completeSprint(@RequestParam(name = "sprintId") Long sprintId){
        try {
            return  ResponseEntity.ok().body(sprintService.completeSprint(sprintId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/start-sprint")
    public ResponseEntity<Object> startSprint(@RequestParam(name = "sprintId") Long sprintId){
        try {
            return  ResponseEntity.ok().body(sprintService.startSprint(sprintId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getDetailSprint(@RequestParam (name = "id") Long sprintId){
        try {
            return  ResponseEntity.ok().body( sprintService.getDetailSprint(sprintId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-sprint-by-project")
    public ResponseEntity<Object> getListSprintByProject(@RequestParam (name = "projectId") Long projectId){
        try {
            return  ResponseEntity.ok().body( sprintService.findSprintByProjectId(projectId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/search-sprint")
    public ResponseEntity<Object> searchSprintPaging( @RequestBody ObjectPaging objectPaging){
        try {
            return  ResponseEntity.ok().body( sprintService.searchSprint(objectPaging));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
