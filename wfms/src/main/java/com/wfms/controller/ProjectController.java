package com.wfms.controller;

import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.ProjectTypeDTO;
import com.wfms.Dto.ProjectUserDTO;
import com.wfms.entity.ProjectType;
import com.wfms.entity.Projects;
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
    public ResponseEntity<Object> createProject(@RequestBody ProjectDTO projectDTO){
        try {
            return  ResponseEntity.ok().body(  projectService.createProject(projectDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-project")
    public ResponseEntity<Object> updateProject(@RequestBody Projects projectDTO){
        try {
            return  ResponseEntity.ok().body( projectService.updateProject(projectDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/list")
    public ResponseEntity<Object> getAllProjectByAdmin(@RequestBody ObjectPaging objectPaging){
        try {
            return  ResponseEntity.ok().body(projectService.findAllProject(objectPaging));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping("/list-by-lead")
    public ResponseEntity<Object> getAllProjectByLead(@RequestHeader("Authorization") String token, @RequestBody ObjectPaging objectPaging){
        try {
            return  ResponseEntity.ok().body(projectService.findAllProjectByLead(token,objectPaging ));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/list-by-member")
    public ResponseEntity<Object> getAllProjectByMember(@RequestHeader("Authorization") String token, @RequestBody ObjectPaging objectPaging){
        try {
            return  ResponseEntity.ok().body(projectService.getProjectByMember(token,objectPaging ));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-detail-project")
    public ResponseEntity<Object> getDetailProject(@RequestParam(name = "projectId") Long projectId){
        try {
            return  ResponseEntity.ok().body(projectService.getDetailProject(projectId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/list-project-type")
    public ResponseEntity<Object> getAllProjectType(){
        try {
            return  ResponseEntity.ok().body(projectTypeService.findAllProjectType());
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create-project-type")
    public ResponseEntity<Object> createProjectType(@RequestBody ProjectTypeDTO projectTypeDTO){
        try {
            return  ResponseEntity.ok().body(  projectTypeService.createProjectType(projectTypeDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-project-type")
    public ResponseEntity<Object> updateProjectType(@RequestBody ProjectType projectTypeDTO){
        try {
            return  ResponseEntity.ok().body( projectTypeService.updateProjectType(projectTypeDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/remove-user-in-project")
    public ResponseEntity<Object> removeUserInProject(@RequestBody ProjectUserDTO projectUsers){
        try {
            return new ResponseEntity<>(projectService.removeUserFromProject(projectUsers),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/add-user-to-project")
    public ResponseEntity<Object> addUserToProject(@RequestBody ProjectUserDTO projectUsers){
        try {
            return new ResponseEntity<>(projectService.addUserToProject(projectUsers),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
