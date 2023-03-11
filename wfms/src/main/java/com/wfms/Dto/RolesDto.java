package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesDto {
    private Long id;
    private String name;
    private Integer status;
    private Date createDate;
    private Date updateDate;
}
