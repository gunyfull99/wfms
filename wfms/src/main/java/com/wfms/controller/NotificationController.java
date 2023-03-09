package com.wfms.controller;

import com.wfms.Dto.NotificationDto;
import com.wfms.service.FireBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    FireBaseService fireBaseService;
    @PostMapping("/send-notification")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationDto notificationDto){
        try {
            String response = fireBaseService.sendNotification(notificationDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/send-many-notification")
    public ResponseEntity<?> sendNotification(@RequestBody List<NotificationDto> notificationDtos){
        try {
            Boolean response = fireBaseService.sendManyNotification(notificationDtos);
            return new ResponseEntity<>("Send all notification success", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
