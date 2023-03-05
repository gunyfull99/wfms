package com.wfms.controller;

import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.ProjectTypeDTO;
import com.wfms.service.ProjectService;
import com.wfms.service.ProjectTypeService;
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
    @Autowired
    private ProjectTypeService projectTypeService;

    @PostMapping("/create-project")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO){
        try{
            projectService.createProject(projectDTO);
            return new ResponseEntity<>(projectDTO,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-project")
    public ResponseEntity<?> updateProject(@RequestBody ProjectDTO projectDTO){
        try{
            projectService.updateProject(projectDTO);
            return new ResponseEntity<>(projectDTO,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/list")
    public ResponseEntity<?> getAllProject(){
        try{
            return new ResponseEntity<>(projectService.findAllProject(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/create-project-type")
    public ResponseEntity<?> createProjectType(@RequestBody ProjectTypeDTO projectTypeDTO){
        try{
            projectTypeService.createProjectType(projectTypeDTO);
            return new ResponseEntity<>(projectTypeDTO,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-project-type")
    public ResponseEntity<?> updateProjectType(@RequestBody ProjectTypeDTO projectTypeDTO){
        try{
            projectTypeService.updateProjectType(projectTypeDTO);
            return new ResponseEntity<>(projectTypeDTO,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
