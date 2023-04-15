package com.wfms.controller;

import com.wfms.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/level")
public class LevelController {
    @Autowired
    private LevelRepository levelRepository;

    @GetMapping("/list")
    public ResponseEntity<Object> getAllLevel(){
        try {
            return new ResponseEntity<>(levelRepository.findAll(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
