package com.wfms.controller;

import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.EventDTO;
import com.wfms.entity.Event;
import com.wfms.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@Slf4j
public class EventControllers {
    @Autowired
    private EventService eventService;
    @PostMapping("/create-event")
    public ResponseEntity<Object> createEvent(@RequestBody EventDTO eventDTO){
        try{
            Event event = eventService.createEvent(eventDTO);
            return  ResponseEntity.ok().body(event);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-event")
    public ResponseEntity<Object> updateEvent(@RequestBody Event eventDTO){
        try{
            Event event = eventService.updateEvent(eventDTO);
            return  ResponseEntity.ok().body(event);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/get-detail")
    public ResponseEntity<Object> getDetail(@RequestParam(name = "eventId") Long scheduleId ){
        try{
            EventDTO schedules = eventService.getDetailEvent(scheduleId);
            return  ResponseEntity.ok().body(schedules);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/search-event")
    public ResponseEntity<Object> searchEvent(@RequestBody ObjectPaging objectPaging){
        try {
            ObjectPaging schedule = eventService.searchEvent(objectPaging);
            return  ResponseEntity.ok().body(schedule);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
