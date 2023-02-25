package com.wfms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
 import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
//(exclude = {SecurityAutoConfiguration.class},scanBasePackages={
//"com.wfms"})
public class WfmsApplication {
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of( "Asia/Ho_Chi_Minh" )));   // It will set UTC timezone
    }
    public static void main(String[] args) {
    SpringApplication.run(WfmsApplication.class, args);

    Double a = (double) (1507 / 10.0);
        System.out.println("zzzzzzzzzzzzzzz "+a);
    }

}
