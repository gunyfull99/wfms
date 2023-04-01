package com.wfms.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.MessageDto;
import com.wfms.entity.DeviceUsers;

import java.util.List;

public interface FireBaseService {
    String sendNotification(MessageDto messageDto) throws FirebaseMessagingException;
    Boolean sendManyNotification(MessageDto messageDtos) throws FirebaseMessagingException;
    DeviceUsers regisFcm(DeviceUsers deviceUsers);
    String deleteFcm(String firebaseToken);
    List<DeviceUsers> listDeviceUsers();


}
