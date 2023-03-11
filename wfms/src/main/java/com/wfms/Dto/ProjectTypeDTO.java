package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProjectTypeDTO {
    private Long projectTypeId;
    private String projectTypeName;
    private Integer status;
    private Date createDate;
    private Date updateDate;
}
