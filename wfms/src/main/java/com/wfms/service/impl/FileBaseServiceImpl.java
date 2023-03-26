package com.wfms.service.impl;

import com.google.firebase.messaging.*;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.entity.DeviceUsers;
import com.wfms.repository.DevicesUsersRepository;
import com.wfms.service.FireBaseService;
import com.wfms.utils.DataUtils;
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
    @Autowired
    private DevicesUsersRepository devicesUsersRepository;
    private String topic = "notification";

    @Override
    public String sendNotification(MessageDto messageDto) throws FirebaseMessagingException {
        List<DeviceUsers> devicesUsers = devicesUsersRepository.findDeviceByUserId(Long.parseLong(messageDto.getTo()));
        List<Message> messageList = new ArrayList<>();
        for (DeviceUsers deviceUsers: devicesUsers) {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(messageDto.getNotification().getTitle())
                            .setBody(messageDto.getNotification().getBody())
                            .build())
                    .setToken(deviceUsers.getDeviceId())
                    .build();
            messageList.add(message);
        }
        BatchResponse response = FirebaseMessaging.getInstance().sendAll(messageList);
        try{
            log.info("Successfully sent message: " + response);
            return "Send message to user id "+ messageDto.getTo()+" success";
        }catch (Exception e){
            return "Send message to user id "+ messageDto.getTo()+" fail";
        }
    }

    @Override
    public Boolean sendManyNotification(List<MessageDto> messageDtos) throws FirebaseMessagingException {
        List<Message> messages = new ArrayList<>();
        Assert.isTrue((messageDtos.size()<500),"Số lượng thông báo gửi đi không được quá 500 thông báo");
        for (MessageDto item: messageDtos) {
            List<DeviceUsers> devicesUsers = devicesUsersRepository.findDeviceByUserId(Long.parseLong(item.getTo()));
            if (DataUtils.notNull(devicesUsers)){
                devicesUsers.forEach(i->{
                    messages.add(Message.builder()
                            .setNotification(Notification.builder()
                                    .setTitle(item.getNotification().getTitle())
                                    .setBody(item.getNotification().getBody())
                                    .build()).setToken(i.getDeviceId()).build());
                });
            }
        }
        BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
        return (response.getSuccessCount() == messageDtos.size());
    }

}
