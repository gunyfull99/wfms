package com.wfms.controller;

import com.wfms.Dto.WorkFlowDTO;
import com.wfms.service.WorkFlowService;
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


    @PostMapping("/create-work-flow")
    public ResponseEntity<Object> createWorkFlow(@RequestBody WorkFlowDTO workFlowDTO){
        try {
            return  ResponseEntity.ok().body(workFlowService.createWorkFlow(workFlowDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @PutMapping("/update-work-flow")
    public ResponseEntity<Object> updateWorkFlow(@RequestBody WorkFlowDTO workFlowDTO){
        try {
            return  ResponseEntity.ok().body( workFlowService.updateWorkFlow(workFlowDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}
