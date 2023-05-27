package com.wfms.Dto;

import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationDto  {
    private String title;
    private String body;
    private Long  taskId;
    private Long  projectId;


}
