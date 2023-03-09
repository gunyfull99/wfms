package com.wfms.controller;

import com.wfms.Dto.WorkFlowStepDTO;
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
    public ResponseEntity<WorkFlowStepDTO> createWorkFlowStep(@RequestBody WorkFlowStepDTO workFlowStep){
        workFlowStepService.createWorkFlowStep(workFlowStep);
        return  ResponseEntity.ok().body(workFlowStep);

    }

    @PutMapping("/update-work-flow-step")
    public ResponseEntity<WorkFlowStepDTO> updateWorkFlowStep(@RequestBody WorkFlowStepDTO workFlowStep){
        workFlowStepService.updateWorkFlowStep(workFlowStep);
        return  ResponseEntity.ok().body(workFlowStep);

    }

    @GetMapping("/list-work-flow-step/{workFlowId}")
    public ResponseEntity<List<WorkFlowStepDTO>> listWorkFlowStepByWorkFlow(@PathVariable(value = "workFlowId") Long workFlowId){
        return  ResponseEntity.ok().body(workFlowStepService.listWorkFlowStep(workFlowId));
    }
}
