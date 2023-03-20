package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wfms.entity.Issue;
import com.wfms.entity.Projects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SprintDTO {
    private Long sprintId;
    private String sprintName;
    private String goal;
    private Double duration;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private Long projectId;
    private Date createDate;
    private Date updateDate;
    private List<Issue> issue ;
}
