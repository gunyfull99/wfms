package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wfms.entity.Projects;
import com.wfms.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

 import java.time.LocalDateTime; 

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DailyReportDTO {
   private Long dailyReportId;
   private String goal;
   private LocalDateTime startDate;
   private LocalDateTime endDateEstimate;
   private Integer statusWork;
   private String problemsEncountered;
   private String note;
   private Double estimateWork;
   private UsersDto memberDoWork;
   private Integer status;
   private LocalDateTime createDate;
   private LocalDateTime updateDate;
   private ProjectDTO projects;
   private TaskDTO task;
}
