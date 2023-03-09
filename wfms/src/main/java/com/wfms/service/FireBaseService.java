package com.wfms.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;

import java.util.List;

public interface FireBaseService {
    String sendNotification(MessageDto messageDto) throws FirebaseMessagingException;
    Boolean sendManyNotification(List<MessageDto> messageDtos) throws FirebaseMessagingException;
}
