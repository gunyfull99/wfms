package com.wfms.service;

import com.wfms.Dto.ObjectPaging;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    String createDocument(String token, Long projectId,String description, List<MultipartFile> files);
    String deleteDocument(String token,Long documentId);
    String updateDocument(Long documentId,String description);
    ObjectPaging getListFileInProject(ObjectPaging objectPaging);
}
