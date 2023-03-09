package com.wfms.controller;

import com.wfms.Dto.WorkFlowDTO;
import com.wfms.Dto.WorkFlowStatusDTO;
import com.wfms.service.WorkFlowService;
import com.wfms.service.WorkFlowStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/workflow")
public class WorkFlowController {

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private WorkFlowStatusService workFlowStatusService;

    @PostMapping("/create-work-flow")
    public ResponseEntity<WorkFlowDTO> createWorkFlow(@RequestBody WorkFlowDTO workFlowDTO){
            workFlowService.createWorkFlow(workFlowDTO);
            return  ResponseEntity.ok().body(workFlowDTO);
    }

    @PostMapping("/create-work-flow-status")
    public ResponseEntity<WorkFlowStatusDTO> createWorkFlow(@RequestBody WorkFlowStatusDTO workFlowStatusDTO){
        workFlowStatusService.createWorkFlowStatus(workFlowStatusDTO);
        return  ResponseEntity.ok().body(workFlowStatusDTO);
    }

    @PutMapping("/update-work-flow-status")
    public ResponseEntity<WorkFlowStatusDTO> updateWorkFlow(@RequestBody WorkFlowStatusDTO workFlowStatusDTO){
        workFlowStatusService.updateWorkFlowStatus(workFlowStatusDTO);
        return  ResponseEntity.ok().body(workFlowStatusDTO);
    }


}
