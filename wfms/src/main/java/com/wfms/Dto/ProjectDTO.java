package com.wfms.Dto;

import com.wfms.entity.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private String shortName;
    private Long lead;
    private String description;
    private Long projectTypeId;
    private List<Long> userId;
    private Date createDate;
    private Integer status;
}
