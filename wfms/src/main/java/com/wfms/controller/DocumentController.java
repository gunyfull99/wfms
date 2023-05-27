package com.wfms.controller;

import com.wfms.Dto.ObjectPaging;
import com.wfms.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping(value = "/create-document",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> createComment(@RequestHeader("Authorization") String token,
                                                @RequestParam(name = "projectId") Long projectId,
                                               @Nullable @RequestParam(name = "description") String description,
                                                @RequestPart List<MultipartFile> files ){
        try {
            return  ResponseEntity.ok().body(  documentService.createDocument(token, projectId,description,files));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/get-list-document-in-project")
    public ResponseEntity<Object> getListDocumentInProject(@RequestBody ObjectPaging objectPaging){
        try {
            ObjectPaging list = documentService.getListFileInProject(objectPaging);
            return  ResponseEntity.ok().body(list);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/delete-document")
    public ResponseEntity<Object> deleteDocument(@RequestHeader("Authorization") String token,@RequestParam(name = "documentId") Long documentId){
        try {
            return  ResponseEntity.ok().body(documentService.deleteDocument(token, documentId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-document")
    public ResponseEntity<Object> updateDocument(@RequestParam(name = "documentId") Long documentId, @Nullable @RequestParam(name = "description") String description){
        try {
            return  ResponseEntity.ok().body(documentService.updateDocument(documentId,description));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
