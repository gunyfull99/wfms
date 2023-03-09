package com.wfms.controller;

import com.google.firebase.messaging.Message;
import com.wfms.Dto.MessageDto;
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
    public ResponseEntity<?> sendNotification(@RequestBody MessageDto messageDto){
        try {
            String response = fireBaseService.sendNotification(messageDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/send-many-notification")
    public ResponseEntity<?> sendNotification(@RequestBody List<MessageDto> messageDtos){
        try {
            Boolean response = fireBaseService.sendManyNotification(messageDtos);
            return new ResponseEntity<>("Send all notification success", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
