package com.wfms.controller;

import com.wfms.Dto.DailyReportDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.DailyReport;
import com.wfms.entity.Note;
import com.wfms.service.DailyReportService;
import com.wfms.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @PostMapping("/create")
    public ResponseEntity<Object> createNote(@RequestBody Note note){
        try {
            return  ResponseEntity.ok().body(noteService.createNote(note));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<Object> getDetailNote(@RequestParam(name = "noteId")Long noteId){
        try {
            return  ResponseEntity.ok().body(noteService.getDetailNote(noteId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update")
    public ResponseEntity<Object> updateNote(@RequestBody Note note){
        try {
            return  ResponseEntity.ok().body(noteService.updateNote(note));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/get-list-note")
    public ResponseEntity<Object> getListNote(@RequestBody ObjectPaging objectPaging){
        try {
            ObjectPaging list = noteService.searchNote(objectPaging);
            return  ResponseEntity.ok().body(list);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
