package com.wfms.utils;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
@Log4j2
@Service
public class MinioUtils {
    @Autowired
    MinioClient minioClient ;
    @Value("${minio.bucket}")
    String defaultBucketName;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    public boolean isObjectExist(String name) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(defaultBucketName)
                    .object(name).build());
            return true;
        } catch (ErrorResponseException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    public String uploadFile(String name, MultipartFile content) {
        try {
            if(isObjectExist(name)){
                name = name.substring(0, name.lastIndexOf("."))
                        + " ("+DataUtils.generateNumber(3)+") "+"."+ name.substring(name.lastIndexOf(".") + 1);
            }
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(defaultBucketName)
                    .object(name)
                    .stream(content.getInputStream(), content.getSize(), -1)
                    .contentType(content.getContentType())
                    .build());
            return name;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public byte[] getFile(String fileName) {
        try {
            InputStream obj = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(defaultBucketName)
                    .object(fileName)
                    .build());

            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFileUrl(String fileName){
        try {
            String url =  minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(defaultBucketName).object(fileName).method(Method.GET).build());
            return url;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
