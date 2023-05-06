package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "note")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long noteId;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "content")
    private String content;
    @Column(name = "title")
    private String title;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "project_id")
    private Long projectId;
}
