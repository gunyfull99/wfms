package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 

@Entity
@Table(name = "document")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;
    @Column(name = "create_user")
    private Long createUser;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "url")
    private String url;
    @Column(name = "type")
    private String type;
    @Column(name = "description")
    private String description;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "status")
    private Integer status;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id")
    private Projects projects;
}
