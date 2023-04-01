package com.wfms.controller1;

import com.wfms.Dto.MessageDto;
import com.wfms.entity.DeviceUsers;
import com.wfms.service.FireBaseService;
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
    public ResponseEntity<Object> deleteFcm(@RequestParam(name = "fcmToken") String fcmToken){
        try {
            return  ResponseEntity.ok().body( fireBaseService.deleteFcm(fcmToken));
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
}
