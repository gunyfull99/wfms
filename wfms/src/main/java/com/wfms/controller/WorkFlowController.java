package com.wfms.controller;

import com.wfms.Dto.WorkFlowDTO;
import com.wfms.service.WorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/workflow")
public class WorkFlowController {

    @Autowired
    private WorkFlowService workFlowService;


    @PostMapping("/create-work-flow")
    public ResponseEntity<WorkFlowDTO> createWorkFlow(@RequestBody WorkFlowDTO workFlowDTO){
            workFlowService.createWorkFlow(workFlowDTO);
            return  ResponseEntity.ok().body(workFlowDTO);
    }
    @PutMapping("/update-work-flow")
    public ResponseEntity<WorkFlowDTO> updateWorkFlow(@RequestBody WorkFlowDTO workFlowDTO){
        workFlowService.updateWorkFlow(workFlowDTO);
        return  ResponseEntity.ok().body(workFlowDTO);
    }


}
