package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedules_id")
    private Long schedulesId;
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
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "project_id")
    private Long projectId;



}
