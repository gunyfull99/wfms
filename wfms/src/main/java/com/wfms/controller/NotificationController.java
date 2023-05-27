package com.wfms.controller;

import com.wfms.Dto.MessageDto;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.DeviceUsers;
import com.wfms.service.FireBaseService;
import com.wfms.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController {
    @Autowired
    FireBaseService fireBaseService;
    @Autowired
    private NotificationService notificationService;
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
    public ResponseEntity<?> sendManyNotification(@RequestBody MessageDto messageDtos){
        try {
            Boolean response = fireBaseService.sendManyNotification(messageDtos);
            return new ResponseEntity<>("Send all notification success", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/regis-fcm")
    public ResponseEntity<Object> registrationFcm(@RequestBody DeviceUsers deviceUsers){
        try {
            return  ResponseEntity.ok().body( fireBaseService.regisFcm(deviceUsers));
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete-fcm")
    public ResponseEntity<Object> deleteFcm(@RequestBody DeviceUsers deviceUsers){
        try {
            return  ResponseEntity.ok().body( fireBaseService.deleteFcm(deviceUsers));
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/list-device")
    public ResponseEntity<Object> listDevice(){
        try {
            return  ResponseEntity.ok().body( fireBaseService.listDeviceUsers());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

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
    @PostMapping("/search")
    public ResponseEntity<?> getNotifiSeen(@RequestHeader("Authorization") String token,@RequestBody ObjectPaging objectPaging ){
        try {
            return new ResponseEntity<>(notificationService.searchNotifcation(objectPaging,token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
