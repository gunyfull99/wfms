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
    private Integer totalIssue;
    private Integer main;
    private Integer closeIssue;
    private Integer notCloseIssue;
    private Integer totalHard;
    private Integer totalMedium;
    private Integer totalEasy;
}
