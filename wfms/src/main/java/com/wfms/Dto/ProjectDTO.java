package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private String shortName;
    private Integer status;
    private String description;
    private Long projectTypeId;
    private LocalDateTime deadLine;
    private UsersDto lead;
    private List<UsersDto> userId;
    private Date createDate;
    private Date updateDate;
    private int totalIssue;
    private Long PriorityId;
}
