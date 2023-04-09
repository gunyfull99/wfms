package com.wfms.service.impl;

import com.google.firebase.messaging.*;
import com.wfms.Dto.MessageDto;
import com.wfms.entity.DeviceUsers;
import com.wfms.repository.DevicesUsersRepository;
import com.wfms.service.FireBaseService;
import com.wfms.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        List<DeviceUsers> devicesUsers = devicesUsersRepository.findDeviceByUserId((messageDto.getUserId().get(0)));
        List<Message> messageList = new ArrayList<>();
        for (DeviceUsers deviceUsers: devicesUsers) {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(messageDto.getNotification().getTitle())
                            .setBody(messageDto.getNotification().getBody())
                            .build())
                    .setToken(deviceUsers.getFirebaseRegistrationToken())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(3600 * 1000)
                            .setNotification(AndroidNotification.builder()
                                    .setIcon("stock_ticker_update")
                                    .setColor("#f45342")
                                    .build())
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setBadge(42)
                                    .build())
                            .build())
                    .build();
            messageList.add(message);
        }
        try{
            firebaseMessaging.sendAll(messageList);
            log.info("Successfully sent message: ");
            return "Send message to user id "+ messageDto.getUserId()+" success";
        }catch (Exception e){
            log.error(e.getMessage());
            return "Send message to user id "+ messageDto.getUserId()+" fail";
        }
    }

    @Override
    public Boolean sendManyNotification(MessageDto messageDtos) throws FirebaseMessagingException {
        List<Message> messages = new ArrayList<>();
        Assert.isTrue(DataUtils.listNotNullOrEmpty(messageDtos.getUserId()),"List user không được để trống");
        Assert.isTrue((messageDtos.getUserId().size()<500),"Số lượng thông báo gửi đi không được quá 500 thông báo");
        for (Long item: messageDtos.getUserId()) {
            List<DeviceUsers> devicesUsers = devicesUsersRepository.findDeviceByUserId(item);
            if (DataUtils.notNull(devicesUsers)){
                devicesUsers.forEach(i->{
                    messages.add(Message.builder()
                            .setNotification(Notification.builder()
                                    .setTitle(messageDtos.getNotification().getTitle())
                                    .setBody(messageDtos.getNotification().getBody())
                                    .build())
                            .setToken(i.getFirebaseRegistrationToken())
                            .setAndroidConfig(AndroidConfig.builder()
                                    .setTtl(3600 * 1000)
                                    .setNotification(AndroidNotification.builder()
                                            .setIcon("stock_ticker_update")
                                            .setColor("#f45342")
                                            .build())
                                    .build())
                            .setApnsConfig(ApnsConfig.builder()
                                    .setAps(Aps.builder()
                                            .setBadge(42)
                                            .build())
                                    .build())
                            .build());
                });
            }
        }
        BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
        return (response.getSuccessCount() == messageDtos.getUserId().size());
    }

    @Override
    public DeviceUsers regisFcm(DeviceUsers deviceUser) {
        Assert.notNull(deviceUser.getUserId(),"User không được để trống");
        Assert.notNull(deviceUser.getDeviceId(),"DeviceId không được để trống");
        Assert.notNull(deviceUser.getFirebaseRegistrationToken(),"FirebaseToken không được để trống");
        List<DeviceUsers> deviceUser1=devicesUsersRepository.findByDeviceId(deviceUser.getDeviceId());
        if(deviceUser1.isEmpty()){
            return devicesUsersRepository.save(deviceUser);
        }
        return null;
    }

    @Override
    public String deleteFcm(String  firebaseToken) {
       try {
           DeviceUsers deviceUsers = devicesUsersRepository.findByToken(firebaseToken);
           devicesUsersRepository.delete(deviceUsers);
           return"Delete token success";
       }catch (Exception e){
           log.error(e.getMessage());
       }
        return"";
    }

    @Override
    public List<DeviceUsers> listDeviceUsers() {
        return devicesUsersRepository.findAll();
    }

}
