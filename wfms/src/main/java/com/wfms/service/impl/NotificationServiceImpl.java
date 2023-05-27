package com.wfms.service.impl;

import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.Notification;
import com.wfms.entity.Users;
import com.wfms.repository.NotificationRepository;
import com.wfms.service.NotificationService;
import com.wfms.service.UsersService;
import com.wfms.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtUtility jwtUtility;
    @Override
    public Long getTotalNotifiNotSeen(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        return notificationRepository.getTotalNotificationWithStatus(users.getId());
    }

    @Override
    public List<Notification> getListNotification(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        return notificationRepository.getNotificationByUserId(users.getId());
    }

    @Override
    public List<Notification> getListNotificationNotSeen(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        return notificationRepository.getNotificationByUserIdAndStatus(users.getId(),1);
    }

    @Override
    public List<Notification> getListNotificationSeen(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        return notificationRepository.getNotificationByUserIdAndStatus(users.getId(),0);
    }

    @Override
    public Notification updateNotificationSeen(Long notificationId) {
        Assert.notNull(notificationId,"NotificationId must not be null");
        notificationRepository.updateStatusByNotificationId(notificationId);
        return notificationRepository.findById(notificationId).get();
    }

    @Override
    public ObjectPaging searchNotifcation(ObjectPaging objectPaging, String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("notificationId").descending());
        Page<Notification> notifications =notificationRepository.getProjectsByMember(users.getId(),objectPaging.getStatus(), Objects.nonNull(objectPaging.getKeyword()) ? objectPaging.getKeyword().toLowerCase() : null,pageable);
        return ObjectPaging.builder().total((int) notifications.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(notifications.getContent()).build();
    }
}
