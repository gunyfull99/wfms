package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "meeting_title")
    private String meetingTitle;
    @Column(name = "meetingDescription")
    private String meetingDescription;
    @Column(name = "room_meeting")
    private String roomMeeting;
    @Column(name = "link_meeting")
    private String linkMeeting;
    @Column(name = "meeting_type")
    private String meetingType;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "status")
    private Integer status;
    @Column(name = "project_id")
    private Long projectId;



}
