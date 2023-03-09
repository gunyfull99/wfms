package com.wfms.service.impl;

import com.google.firebase.messaging.*;
import com.wfms.Dto.NotificationDto;
import com.wfms.service.FireBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileBaseServiceImpl implements FireBaseService {
    @Autowired
    private FirebaseMessaging firebaseMessaging;
    private String topic = "notification";

    @Override
    public String sendNotification(NotificationDto notificationDto) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(notificationDto.getTitle())
                        .setBody(notificationDto.getBody())
                        .build())
                .setToken(notificationDto.getToken())
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        log.info("Successfully sent message: " + response);
        return response;
    }

    @Override
    public Boolean sendManyNotification(List<NotificationDto> notificationDtos) throws FirebaseMessagingException {
        List<Message> messages = new ArrayList<>();
        Assert.isTrue((notificationDtos.size()<500),"Số lượng thông báo gửi đi không được quá 500 thông báo");
        for (NotificationDto item: notificationDtos) {
            messages.add(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(item.getTitle())
                            .setBody(item.getBody())
                            .build()).setToken(item.getToken()).build());
        }
        BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
        return (response.getSuccessCount() == notificationDtos.size());
    }

}
