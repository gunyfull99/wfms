package com.wfms.controller;

import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.RequestTaskDto;
import com.wfms.entity.RequestTask;
import com.wfms.service.DocumentService;
import com.wfms.service.RequestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/requestTask")
public class RequestTaskController {

    @Autowired
    private RequestTaskService requestTaskService;

    @PostMapping(value = "/approve")
    public ResponseEntity<Object> approve( @RequestBody RequestTask requestTask){
        try {
            return  ResponseEntity.ok().body(  requestTaskService.approveRejectRequest(requestTask,2));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/reject")
    public ResponseEntity<Object> reject(@RequestBody RequestTask requestTask){
        try {
            return  ResponseEntity.ok().body(  requestTaskService.approveRejectRequest(requestTask,0));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/search")
    public ResponseEntity<Object> getListDocumentInProject(@RequestBody ObjectPaging objectPaging){
        try {
            ObjectPaging list = requestTaskService.searchRequestTask(objectPaging);
            return  ResponseEntity.ok().body(list);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
