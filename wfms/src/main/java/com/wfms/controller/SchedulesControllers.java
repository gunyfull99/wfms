package com.wfms.controller;

import com.wfms.Dto.SchedulesDTO;
import com.wfms.entity.Schedules;
import com.wfms.service.SchedulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Schedules")
@Slf4j
public class SchedulesControllers {
    @Autowired
    private SchedulesService schedulesService;
    @PostMapping("/create-schedules")
    public ResponseEntity<?> createSchedules(@RequestBody SchedulesDTO schedulesDTO){
        try{
            Schedules schedules = schedulesService.createSchedules(schedulesDTO);
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
