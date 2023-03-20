package com.wfms.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MinioConfig {
    @Value("${minio.access.name}")
    private String accessKey;

    @Value("${minio.access.secret}")
    private String secretKey;

    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.port}")
    private int minioPort;
    @Value("${minio.endpoint}")
    private String minioEndpoint;
    @Value("${minio.ssl}")
    private Boolean minioSsl;
    @Bean
    @Primary
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .endpoint(minioEndpoint,minioPort,minioSsl)
                .credentials(accessKey, secretKey)
                .build();
    }
}
