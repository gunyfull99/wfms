package com.wfms.controller;

import com.wfms.entity.WorkFlowStep;
import com.wfms.service.WorkFlowStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/workflow-step")
public class WorkFlowStepController {

    @Autowired
    private WorkFlowStepService workFlowStepService;

    @PostMapping("/create-work-flow-step")
    public ResponseEntity<Object> createWorkFlowStep(@RequestBody WorkFlowStep workFlowStep){
        try {
            return  ResponseEntity.ok().body( workFlowStepService.createWorkFlowStep(workFlowStep));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-work-flow-step")
    public ResponseEntity<Object> updateWorkFlowStep(@RequestBody WorkFlowStep workFlowStep){
        try {
            return  ResponseEntity.ok().body(workFlowStepService.updateWorkFlowStep(workFlowStep));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list-work-flow-step")
    public ResponseEntity<Object> listWorkFlowStepByWorkFlow(@RequestParam(name = "workFlowId") Long workFlowId){
        try {
            return  ResponseEntity.ok().body(workFlowStepService.listWorkFlowStep(workFlowId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
