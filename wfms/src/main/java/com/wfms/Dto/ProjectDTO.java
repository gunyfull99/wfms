package com.wfms.Dto;

import com.wfms.entity.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private String shortName;
    private Integer status;
    private String description;
    private Long projectTypeId;
    private UsersDto lead;
    private List<UsersDto> userId;
}
