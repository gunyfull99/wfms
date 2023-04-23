package com.wfms.controller;

import com.wfms.Dto.DailyReportDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.DailyReport;
import com.wfms.service.DailyReportService;
import com.wfms.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/dailyReport")
public class DailyReportController {

    @Autowired
    private DailyReportService dailyReportService;

    @PostMapping("/create")
    public ResponseEntity<Object> createDailyReport(@RequestHeader("Authorization") String token,@RequestBody DailyReportDTO dailyReportDTO){
        try {
            return  ResponseEntity.ok().body(dailyReportService.createDailyReport(token,dailyReportDTO));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<Object> getDetailDaiLyReport(@RequestParam(name = "dailyReportId")Long dailyReportId){
        try {
            return  ResponseEntity.ok().body(dailyReportService.getDetailDailyReport(dailyReportId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update")
    public ResponseEntity<Object> updateDaiLyReport(@RequestBody DailyReport dailyReport){
        try {
            return  ResponseEntity.ok().body(dailyReportService.updateDailyReport(dailyReport));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/get-list-daily-report")
    public ResponseEntity<Object> getListDocumentInProject(@RequestBody ObjectPaging objectPaging){
        try {
            ObjectPaging list = dailyReportService.searchDaiLyReport(objectPaging);
            return  ResponseEntity.ok().body(list);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
