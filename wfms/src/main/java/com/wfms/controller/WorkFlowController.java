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
    public ResponseEntity<?> createWorkFlow(@RequestBody WorkFlowDTO workFlowDTO){
        try{
            workFlowService.createWorkFlow(workFlowDTO);
            return new ResponseEntity<>(workFlowDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}
