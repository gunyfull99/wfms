package com.wfms.controller;

import com.wfms.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("notification")
public class NotificationControllers {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/get-total-notifi-not-seen")
    public ResponseEntity<?> getTotalNotifiNotSeen(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(notificationService.getTotalNotifiNotSeen(token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-notification")
    public ResponseEntity<?> getNotification(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(notificationService.getListNotification(token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-notifi-not-seen")
    public ResponseEntity<?> getNotifiNotSeen(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(notificationService.getListNotificationNotSeen(token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-notifi-seen")
    public ResponseEntity<?> getNotifiSeen(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(notificationService.getListNotificationSeen(token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update-notification")
    public ResponseEntity<?> updateNotification(@RequestParam(name = "notificationId") Long notificationId){
        try {
            return new ResponseEntity<>(notificationService.updateNotificationSeen(notificationId),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
