package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueUsersDTO {
    private Long issueUserId;
    private Boolean isResponsible;
    private Date createDate;
    private Date updateDate;
    private Long issueId;
    private Integer status;
    private UsersDto userId;
}
