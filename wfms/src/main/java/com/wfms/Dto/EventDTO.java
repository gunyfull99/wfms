package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventDTO {
    private Long eventId;
    private String meetingTitle;
    private String meetingDescription;
    private String roomMeeting;
    private String linkMeeting;
    private Integer meetingType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer status;
    private ProjectDTO projectDTO;
}
