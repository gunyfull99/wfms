package com.wfms.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.NotificationDto;

import java.util.List;

public interface FireBaseService {
    String sendNotification(NotificationDto notificationDto) throws FirebaseMessagingException;
    Boolean sendManyNotification(List<NotificationDto> notificationDto) throws FirebaseMessagingException;
}
