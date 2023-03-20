package com.wfms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//(exclude = {SecurityAutoConfiguration.class},scanBasePackages={
//"com.wfms"})
public class WfmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WfmsApplication.class, args);
    }

}
