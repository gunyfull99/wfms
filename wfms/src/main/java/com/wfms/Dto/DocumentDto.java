package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wfms.entity.Projects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {
    private Long documentId;
    private UsersDto createUser;
    private String fileName;
    private String url;
    private String type;
    private Date createDate;
    private Date updateDate;
    private Integer status;
    private ProjectDTO projects;
}
