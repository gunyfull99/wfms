package com.wfms.Dto;

import com.wfms.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    private List<Task> task;
}
