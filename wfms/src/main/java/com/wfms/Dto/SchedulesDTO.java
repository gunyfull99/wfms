package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SchedulesDTO {
    private String meetingTitle;
    private String meetingDescription;
    private String roomMeeting;
    private String linkMeeting;
    private Integer meetingType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Date createDate;
    private Date updateDate;
    private Integer status;
    private Long projectId;
}
