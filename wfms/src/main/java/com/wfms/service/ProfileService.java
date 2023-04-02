package com.wfms.service;


import com.wfms.Dto.ResponseValidateUserDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    Resource getFileTemplate(String fileName);
    byte[] updateFileTemplate(Resource file) throws IOException;
    byte[] createTemplateUser(Resource file) throws IOException;
    ResponseValidateUserDTO validateFileCreateUser(MultipartFile files);
}
