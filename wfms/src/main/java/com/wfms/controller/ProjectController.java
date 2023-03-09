package com.wfms.controller;

import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.ProjectTypeDTO;
import com.wfms.entity.Projects;
import com.wfms.service.ProjectService;
import com.wfms.service.ProjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectTypeService projectTypeService;

    @PostMapping("/create-project")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO){
            projectService.createProject(projectDTO);
            return  ResponseEntity.ok().body(projectDTO);
    }
    @PutMapping("/update-project")
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDTO){
            projectService.updateProject(projectDTO);
            return  ResponseEntity.ok().body(projectDTO);
    }
    @GetMapping("/list")
    public ResponseEntity<List<Projects>> getAllProject(){
            return  ResponseEntity.ok().body(projectService.findAllProject());
    }

    @PostMapping("/create-project-type")
    public ResponseEntity<ProjectTypeDTO> createProjectType(@RequestBody ProjectTypeDTO projectTypeDTO){
            projectTypeService.createProjectType(projectTypeDTO);
            return  ResponseEntity.ok().body(projectTypeDTO);
    }

    @PutMapping("/update-project-type")
    public ResponseEntity<ProjectTypeDTO> updateProjectType(@RequestBody ProjectTypeDTO projectTypeDTO){
            projectTypeService.updateProjectType(projectTypeDTO);
            return  ResponseEntity.ok().body(projectTypeDTO);

    }

}
