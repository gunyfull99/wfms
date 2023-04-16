package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class ReportMember {
    private UsersDto users;
    private Integer totalTask;
    private Integer main;
    private Integer closeTask;
    private Integer notCloseTask;
    private Integer totalHard;
    private Integer totalMedium;
    private Integer totalEasy;
}
