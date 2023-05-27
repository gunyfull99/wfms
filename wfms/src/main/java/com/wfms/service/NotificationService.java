package com.wfms.service;

import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.Notification;

import java.util.List;

public interface NotificationService {
    Long getTotalNotifiNotSeen(String token);
    List<Notification> getListNotification(String token);
    List<Notification> getListNotificationNotSeen(String token);
    List<Notification> getListNotificationSeen(String token);
    Notification updateNotificationSeen(Long notificationId);
    ObjectPaging searchNotifcation(ObjectPaging objectPaging,String token);

}
