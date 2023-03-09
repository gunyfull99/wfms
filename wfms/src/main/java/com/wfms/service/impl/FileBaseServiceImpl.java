package com.wfms.service.impl;

import com.google.firebase.messaging.*;
import com.wfms.Dto.MessageDto;
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
    public String sendNotification(MessageDto messageDto) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(messageDto.getNotification().getTitle())
                        .setBody(messageDto.getNotification().getBody())
                        .build())
                .setToken(messageDto.getTo())
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        log.info("Successfully sent message: " + response);
        return response;
    }

    @Override
    public Boolean sendManyNotification(List<MessageDto> messageDtos) throws FirebaseMessagingException {
        List<Message> messages = new ArrayList<>();
        Assert.isTrue((messageDtos.size()<500),"Số lượng thông báo gửi đi không được quá 500 thông báo");
        for (MessageDto item: messageDtos) {
            messages.add(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(item.getNotification().getTitle())
                            .setBody(item.getNotification().getBody())
                            .build()).setToken(item.getTo()).build());
        }
        BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
        return (response.getSuccessCount() == messageDtos.size());
    }

}
