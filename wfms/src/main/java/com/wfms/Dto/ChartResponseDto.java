package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChartResponseDto {
     private Double month;
     private Double totalTask;
     private Double totalTaskDone;
     private Double totalTaskNotDone;
}
