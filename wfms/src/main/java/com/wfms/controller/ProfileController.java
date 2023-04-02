package com.wfms.controller;

import com.wfms.service.ProfileService;
import com.wfms.utils.Constants;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.InputStream;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @GetMapping("/download-template")
    public ResponseEntity<Object> downloadUserTemplate(@RequestParam("fileName") String fileName){
        try {
        Resource file = profileService.getFileTemplate(fileName);
        InputStream inputStream = file.getInputStream();
            byte[] bytesFile = IOUtils.toByteArray(inputStream);
            if (Objects.requireNonNull(file.getFilename()).contains(Constants.FILE_USER_NAME)){
                bytesFile = profileService.updateFileTemplate(file);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("File",file.getFilename());
            headers.set("Content-Disposition","attachment; filename="+file.getFilename());
            headers.set("Access-Control-Expose-Headers", "File");
            return new ResponseEntity<>(bytesFile,headers,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
