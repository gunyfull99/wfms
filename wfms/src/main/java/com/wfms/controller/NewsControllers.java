package com.wfms.controller;

import com.wfms.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("news")
public class NewsControllers {
    @Autowired
    private NewsService newsService;
    @GetMapping("/get-total-notifi-not-seen")
    public ResponseEntity<?> getTotalNotifiNotSeen(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(newsService.getTotalNotifiNotSeen(token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-notification")
    public ResponseEntity<?> getNotification(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(newsService.getListNews(token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-notifi-not-seen")
    public ResponseEntity<?> getNotifiNotSeen(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(newsService.getListNewsNotSeen(token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-notifi-seen")
    public ResponseEntity<?> getNotifiSeen(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(newsService.getListNewsSeen(token),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update-notification")
    public ResponseEntity<?> updateNotification(@RequestParam(name = "newsId") Long newsId){
        try {
            return new ResponseEntity<>(newsService.updateNewsSeen(newsId),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
