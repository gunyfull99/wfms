package com.wfms.controller;

import com.wfms.entity.WorkFlowStep;
import com.wfms.service.WorkFlowStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/workflow-step")
public class WorkFlowStepController {

    @Autowired
    private WorkFlowStepService workFlowStepService;

    @PostMapping("/create-work-flow-step")
    public ResponseEntity<WorkFlowStep> createWorkFlowStep(@RequestBody WorkFlowStep workFlowStep){
        workFlowStepService.createWorkFlowStep(workFlowStep);
        return  ResponseEntity.ok().body(workFlowStep);

    }

    @PutMapping("/update-work-flow-step")
    public ResponseEntity<WorkFlowStep> updateWorkFlowStep(@RequestBody WorkFlowStep workFlowStep){
        workFlowStepService.updateWorkFlowStep(workFlowStep);
        return  ResponseEntity.ok().body(workFlowStep);

    }

    @GetMapping("/list-work-flow-step/{workFlowId}")
    public ResponseEntity<List<WorkFlowStep>> listWorkFlowStepByWorkFlow(@PathVariable(value = "workFlowId") Long workFlowId){
        return  ResponseEntity.ok().body(workFlowStepService.listWorkFlowStep(workFlowId));
    }
}
