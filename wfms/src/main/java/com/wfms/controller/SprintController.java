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
    public ResponseEntity<SprintDTO> createSprint(@RequestBody SprintDTO sprintDTO){
            sprintService.createSprint(sprintDTO);
            return  ResponseEntity.ok().body(sprintDTO);

    }

    @PutMapping("/update-sprint")
    public ResponseEntity<SprintDTO> updateSprint(@RequestBody SprintDTO sprintDTO){
            sprintService.updateSprint(sprintDTO);
            return  ResponseEntity.ok().body(sprintDTO);

    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintDTO> getDetailSprint(@PathVariable (value = "id") Long sprintId){
            return  ResponseEntity.ok().body( sprintService.getDetailSprint(sprintId));
    }
}
