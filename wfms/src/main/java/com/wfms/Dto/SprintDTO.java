package com.wfms.Dto;

import com.wfms.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 
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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer status;
    private Long projectId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<Task> task;
}
