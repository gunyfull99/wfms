package com.wfms.controller;

import com.wfms.Dto.ProjectDTO;
import com.wfms.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @PostMapping("/create-project")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO){
        try{
            projectService.createProject(projectDTO);
            return new ResponseEntity<>(projectDTO,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
}
