package com.wfms.service;


import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ProfileService {
    Resource getFileTemplate(String fileName);
    byte[] updateFileTemplate(Resource file) throws IOException;
}
