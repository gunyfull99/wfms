package com.wfms.service;

import com.wfms.Dto.ObjectPaging;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    String createDocument(String token,Long projectId,List<MultipartFile> files);
    String deleteDocument(String token,Long documentId);
    ObjectPaging getListFileInProject(ObjectPaging objectPaging);
}
